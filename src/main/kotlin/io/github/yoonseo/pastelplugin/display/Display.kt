package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.Delay
import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.debug
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import javax.xml.stream.events.Comment
import kotlin.math.PI

/*
* first array means Y
* second means X */
typealias SimpleDisplayingMap = ArrayList<ArrayList<Char>>
typealias ElementNodeLambda = ElementNode.() -> Unit
class Display {
    var size : DisplaySize = DisplaySize(0f,0f)

    private val elementList = ArrayList<DisplayElement>()

    fun addElement(element: DisplayElement){
        elementList.add(element)
    }
    fun addAllElement(elements: ArrayList<DisplayElement>){
        elementList.addAll(elements)
    }
    fun summon(loc : Location) {
        val a = loc.clone()
        a.pitch = 0f
        elementList.forEach {
            it.summon(it.pos.applyToLocation(a))
        }
    }

    fun close(){
        elementList.forEach { it.close() }
    }



}
fun test() : Display{
    return display {
        initialize(5f sizedBy 5f)

        displayedBySimpleMap(
            "N  N  N  E  N  N  N",
            "N  N  N  N  N  N  N",
            "N  N  N  N  N  N  N",
            "N  N  N  N  N  N  N",
            "N  N  N  N  N  N  N",
            "N  N  N  N  N  N  N",
            "N  N  N  N  N  N  N"
        ){
            val a = listingElement("", list = arrayListOf("a","b","c"))
            Delay(50){

                    a.update(arrayListOf("a","c"))

            }
        }
    }
}


interface Node{
    val registered : ArrayList<DisplayElement>
}
class TopNode(val display : Display) : Node{
    override val registered = ArrayList<DisplayElement>()
    fun initialize(size: DisplaySize){
        display.size = size
    }
    fun elements(elementNode: ElementNodeLambda){
        display.addAllElement(quickEject(display,elementNode))
    }
    fun displayedBySimpleMap(vararg lines : String, elementNode: ElementNodeLambda) : SimpleDisplayingMap {
        val map = SimpleDisplayingMap()

        for((i,e) in lines.withIndex()){
            map.add(ArrayList())
            for(element in e){
                if(element != 'E' && element != 'N') continue
                map[i].add(element)
            }
        }

        registered.addAll(quickEject(display,elementNode))


        val mapHeight = map.size; val mapWidth = map.first().size

        val pixelHeight = display.size.height/mapHeight
        val pixelWidth = display.size.width/mapWidth
        debug("$pixelHeight,$pixelWidth")

        var displayElementNumber = 0
        for(height in map.indices){
            require(mapHeight == map[height].size)//validation height == width

            for((width,element) in map[height].withIndex()){//위치 적용시키는 코드
                if(element == 'E'){
                    try{
                        registered[displayElementNumber].pos = DisplayPosition(height*pixelHeight,width*pixelWidth)
                    }catch (e : IndexOutOfBoundsException){
                        Bukkit.getLogger().warning("number of defined SimpleDisplayedMap's element is not correspond with given number of element")
                    }
                    displayElementNumber++
                }
            }

        }


        display.addAllElement(registered)
        return map
    }

    fun setEventReceiver(){

    }
}

class ElementNode(val display : Display) : Node{
    override val registered = ArrayList<DisplayElement>()

    fun interactiveElement( elementName: String,pos: DisplayPosition = DisplayPosition.ZERO,elementNode: ElementNodeLambda) : InteractiveElement{
        return InteractiveElement(display,pos,elementName).also {registered.add(it); it.inners = quickEject(display,elementNode)}
    }

    fun textElement(elementName: String, text : TextComponent, textWidth : Float, textHeight : Float = TextElement.HEIGHT,pos: DisplayPosition = DisplayPosition.ZERO) : TextElement{
        return TextElement(display,pos,elementName,text,textWidth, textHeight).also {registered.add(it)}
    }

    fun <T> listingElement(elementName: String,pos: DisplayPosition = DisplayPosition.ZERO,list: ArrayList<T>) : ListingElement<T>{
        return ListingElement(display,pos,elementName,list).also { registered.add(it) }
    }


}

private fun quickEject(display : Display,elementNode: ElementNodeLambda) : ArrayList<DisplayElement> {
    val temp = ElementNode(display)
    elementNode(temp)
    return temp.registered
}

fun display(topNode: TopNode.() -> Unit) : Display{
    val display = Display()
    val node = TopNode(display)
    topNode(node)
    return display
}



data class DisplayPosition(val x : Float,val y : Float){
    companion object {val ZERO = DisplayPosition(0f,0f)}
    fun applyToLocation(target : Location) : Location {
        return target.clone()
            .add(0.0, y.toDouble(),0.0)
            .add(target.direction.rotateAroundY(PI/2).multiply(x))
    }


}
data class DisplaySize(val width : Float,val height : Float)
infix fun Float.sizedBy(f : Float) = DisplaySize(this,f)

interface Extendable {
    val inners : ArrayList<DisplayElement>
}