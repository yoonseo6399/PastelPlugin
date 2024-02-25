package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.debug
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
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E",
            "E  E  E  E  E  E  E"
        ){

            repeat(49){
                textElement("A",Component.text(it),TextElement.HEIGHT)
            }
//            interactiveElement("interactive"){
//                textElement("quit",Component.text("[X]"),TextElement.HEIGHT)
//            }.whenInteracted {
//                display.close()
//            }
//            interactiveElement("interactive"){
//                textElement("text",Component.text("경대영"),1f)
//            }.whenInteracted {
//                it.sendMessage("바보")
//            }
//            interactiveElement("interactive"){
//                textElement("text",Component.text("바보"),1f)
//            }.whenInteracted {
//                it.sendMessage("경대영")
//            }
        }
    }
}


interface Node
class TopNode(val display : Display) : Node{
    internal val registered = ArrayList<DisplayElement>()
    fun initialize(size: DisplaySize){
        display.size = size
    }
    fun elements(elementNode: ElementNodeLambda){

    }
    fun displayedBySimpleMap(vararg lines : String, elementNode: ElementNodeLambda) : SimpleDisplayingMap{
        val map = SimpleDisplayingMap()

        for((i,e) in lines.withIndex()){
            map.add(ArrayList())
            for(element in e){
                if(element != 'E' && element != 'N') continue
                map[i].add(element)
            }
        }

        registered.addAll(quickEject(display,this,elementNode))


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

class ElementNode(val display : Display,val superNode: Node) : Node{
    internal val registered = ArrayList<DisplayElement>()

    fun interactiveElement( elementName: String,pos: DisplayPosition = DisplayPosition.ZERO,elementNode: ElementNodeLambda) : InteractiveElement{
        return InteractiveElement(display,pos,elementName).also {registered.add(it); it.inners = quickEject(display,this,elementNode)}
    }

    fun textElement(elementName: String, text : TextComponent, textWidth : Float, textHeight : Float = TextElement.HEIGHT,pos: DisplayPosition = DisplayPosition.ZERO) : TextElement{
        return TextElement(display,pos,elementName,text,textWidth, textHeight).also {registered.add(it)}
    }


}

private fun quickEject(display : Display,superNode : Node,elementNode: ElementNodeLambda) : ArrayList<DisplayElement> {
    val temp = ElementNode(display,superNode)
    elementNode(temp)
    return temp.registered
}

fun display(topNode: TopNode.() -> Unit) : Display{
    val display = Display()
    val node = TopNode(display)
    topNode(node)
    return display
}

/*
* display {
*   InteractiveDisplay { // extendable
*       TextElement("")
*
*   }
* */

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