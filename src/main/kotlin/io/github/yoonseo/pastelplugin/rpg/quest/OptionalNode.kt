package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.mergeWithSeparator
import io.github.yoonseo.pastelplugin.waitForCondition
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player

class OptionalNode {
    val options = hashMapOf<Int,Pair<Component, ConversationNode>>()
    var isChosen = false
    var chosenOption : ConversationNode? = null
    var choiceNumber = 1

    fun choice(choiceName: Component, node : ConversationNode.()->Unit){
        options[choiceNumber] = choiceName to ConversationNode().also {node(it)}
        choiceNumber++
    }

    suspend fun optionPrintAndAwait(player: Player){

        val optionalComponents = options.map { it.value.first.clickEvent(ClickEvent.runCommand("/quest choose ${it.key}")) }


        player.sendMessage(optionalComponents.mergeWithSeparator(Component.text(" ")))

        QuestCommand.addOptionalQuest(player, this)
        waitForCondition {
            isChosen
        }
        chosenOption!!.print(player)
    }

    fun choose(optionNumber : Int) : Boolean{
        if (options[optionNumber] != null){
            chosenOption = options[optionNumber]!!.second
            isChosen = true
            return true
        }else return false
    }
}