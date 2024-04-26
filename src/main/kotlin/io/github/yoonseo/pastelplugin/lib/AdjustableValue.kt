package io.github.yoonseo.pastelplugin.lib

import io.github.yoonseo.pastelplugin.debug
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AdjustableValue<T : Any?>(private val name: String,private var value: T,val onChange: (T,T) -> Boolean = {_, _ -> true}) : ReadWriteProperty<Any?,T> {

    companion object{
        private val list = ArrayList<AdjustableValue<*>>()
        fun getInstance(name: String) = list.firstOrNull { it.name == name }
    }
    init {
        list.add(this)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if(onChange(this.value,value)) this.value = value
    }

    fun getValue() = value
    @Suppress("UNCHECKED_CAST")
    fun commandedChange(string: String,type: String) : Boolean{
        val typedValue = try { string.toType(type) } catch (e : Exception) { debug("c") ;return false}

        value = typedValue as? T ?: return false
        return true
    }
}

fun <T : Any> adjustableValue(name: String,initValue: T,onChange: (T,T) -> Boolean = {_, _ -> true}) = AdjustableValue(name,initValue,onChange)


@Suppress("FunctionName")
infix fun <T : Any> String.AdjustableOf(initValue: T) = adjustableValue(this,initValue)
fun getValueOfAdjustable(name: String) = AdjustableValue.getInstance(name)?.getValue()