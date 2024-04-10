package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.mergeWithSeparator
import io.github.yoonseo.pastelplugin.waitForCondition
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player

class OptionalNode {
    val options = hashMapOf<Component, ConversationNode>()
    var isChosen = false
    var chosenOption : ConversationNode? = null

    fun choice(choiceName: Component, node : ConversationNode.()->Unit){
        options[choiceName] = ConversationNode().also {node(it)}
    }

    suspend fun optionPrintAndAwait(player: Player){

        val optionalComponents = options.keys.map { it.clickEvent(ClickEvent.runCommand("/quest choose ${(it as? TextComponent)?.content()}")) }


        player.sendMessage(optionalComponents.mergeWithSeparator(Component.text(" ")))

        QuestCommand.addOptionalQuest(player, this)
        waitForCondition {
            isChosen
        }
        chosenOption!!.print(player)
    }

    fun choose(optionName : String){
        options.mapNotNull {
            entry -> (entry.key as? TextComponent)?.content()?.let { it to entry.value  }
        }.toMap()[optionName]?.let { chosenOption = it }
        isChosen = true
    }
}