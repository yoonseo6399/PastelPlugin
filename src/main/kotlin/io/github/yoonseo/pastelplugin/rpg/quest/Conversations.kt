package io.github.yoonseo.pastelplugin.rpg.quest


import io.github.yoonseo.pastelplugin.mergeWithSeparator
import io.github.yoonseo.pastelplugin.plus
import io.github.yoonseo.pastelplugin.waitForCondition
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player


class ConversationNode{
    private val conversation = arrayListOf<Component>()
    private var optionalNode : OptionalNode? = null
    private var executes : ExecutableNode? = null
    fun Npc.talk(message: Component){
        conversation.add(name + Component.text(" ") + message)
    }

    fun option(optionNode : OptionalNode.() -> Unit){
        optionalNode = OptionalNode()
        optionNode(optionalNode!!)
    }

    fun executes(executableNode: ExecutableNode.() -> Unit){

    }

    suspend fun print(player: Player){
        conversation.printTo(player)
        optionalNode?.optionPrintAndAwait(player)
        executes?.invoke()
    }
}

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

        QuestCommand.addOptionalQuest(player,this)
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