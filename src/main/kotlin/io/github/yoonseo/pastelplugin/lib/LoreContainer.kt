package io.github.yoonseo.pastelplugin.lib

import kotlin.reflect.KProperty
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.w3c.dom.Text
import kotlin.reflect.KClass

class LoreContainer(private val item: ItemStack) {
    init{
        if(!item.hasItemMeta()) throw IllegalStateException("Item has no itemMeta")


    }
    val context by lazy {
        getAllNameSpace().map { it to load(it)!! }.let { temp ->
            val map = HashMap<String,String>()

            temp.forEach{
                map[it.first] = it.second
            }
            MapContext(map)
        }
    }
//
//    fun <T : Any> context(type: KClass<T>): MapContext<T>{
//
//    }

    private fun getAllNameSpace(): List<String>{
        val lores = item.itemMeta.lore()!!
        val pattern = Regex("[a-zA-Z]\\s*:\\s*[a-zA-Z]")
        return lores.mapNotNull { (it as? TextComponent)?.content() }.filter { pattern.matches(it) }
    }
    fun load(nameSpace: String): String?{
        val lore = item.itemMeta.lore()!!.find { (it as TextComponent).content().startsWith(nameSpace) } as? TextComponent
        if (lore != null) {
            return lore.content().substring(nameSpace.length+3)
        }
        return null
    }
    fun store(nameSpace: String,value:String){
        val lore = item.itemMeta.lore()!!.find { (it as TextComponent).content().startsWith(nameSpace) } as? TextComponent
        if(lore == null){
            val meta = item.itemMeta!!
            val lores = meta.lore()!!
            lores.add(Component.text("$nameSpace : $value"))
            meta.lore(lores)
            item.itemMeta = meta
            return
        }
        val meta = item.itemMeta!!
        val index = item.itemMeta.lore()!!.indexOf(lore)
        val lores = meta.lore()!!
        lores[index] = Component.text("$nameSpace : $value")
        meta.lore(lores)
        item.itemMeta = meta
    }
    fun loreInit(){
        if(item.itemMeta.lore()?.isEmpty() == false){
            item.itemMeta.lore(emptyList())
        }
    }
}


val ItemStack.loreContainer: LoreContainer
    get() = LoreContainer(this)




class MapContext(val map: Map<String,String>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return map[property.name] ?: throw IllegalArgumentException("Cannot find property named ${property.name}")
    }
    inline operator fun <reified T : Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        return map[property.name]?.toType(T::class) ?: throw IllegalArgumentException("Cannot find property named ${property.name}")
    }

    operator fun get(index: String) =
            map[index]
    fun getNotNull(index: String) =
            map[index]!!

}

fun <T : Any> String.toType(kClass: KClass<T>): T {
    return when (kClass.simpleName) {

        "Int" -> this.toInt()
        "Long" -> this.toLong()
        "Float" -> this.toFloat()
        "Double" -> this.toDouble()
        "String" -> this.toString()
        "Boolean" -> if(equals("true")) true else if(equals("false")) false else throw NumberFormatException()

        "int" -> this.toInt()
        "long" -> this.toLong()
        "float" -> this.toFloat()
        "double" -> this.toDouble()
        "string" -> this.toString()
        "boolean" -> if(equals("true")) true else if(equals("false")) false else throw NumberFormatException()



        else -> throw ClassCastException("Cannot cast String to type, Maybe Command's declaration's problem, Requesting Type Must be default type(example. Int,String ---)")
    } as T
}