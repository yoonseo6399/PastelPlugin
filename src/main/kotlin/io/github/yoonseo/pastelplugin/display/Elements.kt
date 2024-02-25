package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.spawn
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.w3c.dom.Text

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
    fun whenInteracted(task : (Player) -> Unit){// callback 함수 지정
        whenInteracted = task
    }
    override fun summon(summonLocation: Location) {
        inners.forEach { it.positionedSummon(summonLocation) }


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

    fun text(component: TextComponent?){
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

class ListingElement<T : Any?>(
    display: Display,
    pos: DisplayPosition,
    elementName: String,
    val list: ArrayList<T>
) : DisplayElement(display,pos,elementName){
    val listingElement = ArrayList<TextElement>(list.size+1)


    override fun summon(summonLocation: Location) {

        for((i,o) in list.withIndex()){ // 리스트 소환하기
            debug("y")
            listingElement.add(
                TextElement(//list 를 띄워줄 엘리먼트
                    display,
                    DisplayPosition(0f,-TextElement.HEIGHT*i),
                    "$this-listingText-$i",
                    MiniMessage.miniMessage().deserialize(o.toString()) as TextComponent,
                    0f,0f
                ).also{it.positionedSummon(summonLocation)}//소환
            )
        }
    }

    fun update(list : ArrayList<T>){
        reduceElementIfNeed(list)
        increaseElementIfNeed(list)// 스파게티
        for((element,index) in findNeedUpdateElementsWithIndex(list)){
            element.text(MiniMessage.miniMessage().deserialize(list[index].toString()) as TextComponent)
        }
    }

    private fun reduceElementIfNeed(list : ArrayList<T>){
        if(list.size < listingElement.size) {

            val removedElement = listingElement.filterIndexed { index, textElement ->
                if(index > list.size-1) {//만약 list의 길이가 소환된 엘리먼트보다 작다면 엘리면트도 제거 ( size 는 1부터 시작하기떄문에 -1을 더해줌
                    textElement.close()
                    true
                }else false
            }
            listingElement.removeAll(removedElement.toSet())
        }
    }
    private fun increaseElementIfNeed(list : ArrayList<T>){

    }


    //만약 list의 길이가 소환된 엘리먼트보다 작다면 엘리면트도 제거
    private fun findNeedUpdateElementsWithIndex(list : ArrayList<T>) : List<Pair<TextElement,Int>> {
        val refinedList = aaa(list)




        return listingElement.mapIndexedNotNull { index, textElement ->
            //debug("map - $index")
            if(textElement.text != refinedList[index]) textElement to index
            else null
        }
    }

    private fun aaa(list : ArrayList<T>) =
        list.map { MiniMessage.miniMessage().deserialize(it.toString()) as TextComponent
    }



    override fun close() {
        listingElement.forEach { it.close() }
    }

    override fun getSize(): DisplaySize {
        TODO("Not yet implemented")
    }

}

abstract class DisplayElement(val display : Display, var pos : DisplayPosition, val elementName : String){
    abstract fun summon(summonLocation : Location)
    fun positionedSummon(summonLocation : Location){
        summon(pos.applyToLocation(summonLocation))
    }

    abstract fun close()

    abstract fun getSize() : DisplaySize
}