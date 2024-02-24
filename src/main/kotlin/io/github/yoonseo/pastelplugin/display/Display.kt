package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.spawn
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Vector
import java.util.EventListener
import kotlin.math.PI

/*
* first array means Y
* second means X */
typealias DisplayingMap = ArrayList<ArrayList<Char>>
class Display {

    private val elementList = ArrayList<DisplayElement>()

    fun addElement(element: DisplayElement){
        elementList.add(element)
    }
    fun summon(loc : Location) {
        elementList.forEach {
            it.summon(it.pos.applyToLocation(loc))
        }
    }

    fun close(){
        elementList.forEach { it.close() }
    }

    fun interactiveElement()

}
fun test(){
    display {
        define {
            displayingMap(
                "        E",
                "         ",
                "    E    ",
                "    E    ",
                "         ",
            )
        }
        addElements {

        }
    }
}

class TopNode{
    fun define(destinationNode: DestinationNode.() -> DisplayingMap){

    }
    fun addElements(bottomNode: BottomNode.() -> Unit){

    }
}
class DestinationNode{
    fun displayingMap(vararg lines : String) : DisplayingMap{
        val map = DisplayingMap()

        for((i,e) in lines.withIndex()){
            for(element in e){
                if(element != ' ' && element != 'E') throw IllegalArgumentException("only allowed ' ' and 'E'")
                map[i].add(element)
            }
        }
        return map
    }
}
class BottomNode{

}

fun display(topNode: TopNode.() -> Unit) : Display{
    val temp = Display()
    topNode(temp)
    return temp
}

/*
* display {
*   InteractiveDisplay { // extendable
*       TextElement("")
*
*   }
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
* */










class InteractiveElement(
    pos: DisplayPosition,
    elementName: String,
    val innerElement: DisplayElement?
) : DisplayElement(pos, elementName) ,Extendable
{
    lateinit var whenInteracted : (Player) -> Unit

    lateinit var interaction : Interaction
    private var lastInteractedTime : Long = -1
    var eventListenerTask : Int = -1
    fun whenInteracted(task : (Player) -> Unit){
        whenInteracted = task
    }
    override fun summon(summonLocation: Location) {
        innerElement?.summon(summonLocation)


        interaction = summonLocation.spawn(Interaction::class)

        interaction.apply {
            setRotation(summonLocation.yaw,0f)
            isResponsive = true

            if(innerElement != null){
                interactionWidth = innerElement.getSize().width
                interactionHeight = innerElement.getSize().height

            }else {
                interactionWidth = 1f;interactionHeight = 1f
            }
            //interactionWidth = 1f;interactionHeight = 1f
        }

        //interaction 관리
        eventListenerTask = ScheduleRepeating {
            if(interaction.lastInteraction != null && interaction.lastInteraction!!.timestamp > lastInteractedTime){
                whenInteracted(interaction.lastInteraction!!.player.player!!)
                lastInteractedTime = interaction.lastInteraction!!.timestamp
            }
        }
    }

    override fun close() {
        innerElement?.close()
        interaction.remove()
    }

    override fun getSize(): DisplaySize = innerElement?.getSize() ?: DisplaySize(interaction.interactionWidth,interaction.interactionHeight)

}
class TextElement(pos: DisplayPosition, elementName: String,val text : TextComponent,val textWidth : Float,val textHeight : Float = HEIGHT) : DisplayElement(pos, elementName) {
    lateinit var textDisplay : TextDisplay
    companion object{
        const val HEIGHT = 0.275f
    }

    fun showText(component: TextComponent?){
        textDisplay.text(component)
    }

    override fun summon(summonLocation: Location) {
        textDisplay = summonLocation.spawn(TextDisplay::class)
        textDisplay.apply {
            text(this@TextElement.text)
            setRotation(summonLocation.yaw,0f)

            displayWidth = textWidth
            displayHeight = textHeight
        }
    }

    override fun close() {
        textDisplay.remove()
    }

    override fun getSize(): DisplaySize = DisplaySize(textDisplay.displayWidth,textDisplay.displayHeight)

}


data class DisplayPosition(val x : Float,val y : Float){
    fun applyToLocation(target : Location) : Location {
        return target.clone()
            .add(0.0, y.toDouble(),0.0)
            .add(target.direction.rotateAroundY(PI/2).multiply(x))
    }


}
data class DisplaySize(val width : Float,val height : Float)
abstract class DisplayElement(val pos : DisplayPosition,val elementName : String){
    abstract fun summon(summonLocation : Location)

    abstract fun close()

    abstract fun getSize() : DisplaySize
}

interface Extendable {
    operator fun invoke(extendingNode : (Unit) -> DisplayElement)
}