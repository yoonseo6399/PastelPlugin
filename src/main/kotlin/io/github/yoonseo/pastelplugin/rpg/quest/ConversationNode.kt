package io.github.yoonseo.pastelplugin.rpg.quest


import io.github.yoonseo.pastelplugin.plus
import net.kyori.adventure.text.Component
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

