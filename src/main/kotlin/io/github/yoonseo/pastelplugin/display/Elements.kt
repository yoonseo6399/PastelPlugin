package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.spawn
import net.kyori.adventure.text.TextComponent
import org.bukkit.Location
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

class InteractiveElement(
    display: Display,
    pos: DisplayPosition,
    elementName: String,
) : DisplayElement(display,pos, elementName) , Extendable
{



    override var inners: ArrayList<DisplayElement> = ArrayList()
    lateinit var whenInteracted : (Player) -> Unit

    lateinit var interaction : Interaction
    private var lastInteractedTime : Long = -1
    var eventListenerTask : Int = -1
    fun whenInteracted(task : (Player) -> Unit){
        whenInteracted = task
    }
    override fun summon(summonLocation: Location) {
        inners.forEach { it.summon(summonLocation) }


        interaction = summonLocation.spawn(Interaction::class)

        interaction.apply {
            setRotation(summonLocation.yaw,0f)
            isResponsive = true

            if(inners.firstOrNull() != null){
                interactionWidth = inners.firstOrNull()!!.getSize().width
                interactionHeight = inners.firstOrNull()!!.getSize().height

            }else {
                interactionWidth = 1f;interactionHeight = 1f
            }
            //interactionWidth = 1f;interactionHeight = 1f
        }

        //interaction 관리
        eventListenerTask = ScheduleRepeating {
            if (interaction.lastInteraction != null && interaction.lastInteraction!!.timestamp > lastInteractedTime) {
                whenInteracted(interaction.lastInteraction!!.player.player!!)
                lastInteractedTime = interaction.lastInteraction!!.timestamp
            }
        }
    }

    override fun close() {
        inners.forEach{ it.close() }
        interaction.remove()
    }

    override fun getSize(): DisplaySize = inners.firstOrNull()?.getSize() ?: DisplaySize(
        interaction.interactionWidth,
        interaction.interactionHeight
    )

}

class TextElement(
    display: Display,
    pos: DisplayPosition,
    elementName: String,
    val text : TextComponent,
    val textWidth : Float,
    val textHeight : Float = HEIGHT
) : DisplayElement(display,pos, elementName)
{


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

    override fun getSize(): DisplaySize = DisplaySize(textDisplay.displayWidth, textDisplay.displayHeight)

}

abstract class DisplayElement(val display : Display, var pos : DisplayPosition, val elementName : String){
    abstract fun summon(summonLocation : Location)

    abstract fun close()

    abstract fun getSize() : DisplaySize
}