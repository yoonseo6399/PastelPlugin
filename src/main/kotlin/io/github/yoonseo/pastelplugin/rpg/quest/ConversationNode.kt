package io.github.yoonseo.pastelplugin.rpg.quest


import io.github.yoonseo.pastelplugin.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player


class ConversationNode{
    private val conversation = arrayListOf<Component>()
    private var optionalNode : OptionalNode? = null
    private var executes : ExecutableNode? = null
    fun Npc.talk(message: Component){
        conversation.add(Component.text("[ ") + name + Component.text(" ] ") + message)
    }
    fun Npc.talk(message: String){
        talk(Component.text(message))
    }
    fun tell(message: Component){
        conversation.add(message)
    }
    fun tell(message: String){
        tell(Component.text(message))
    }
    fun QuestScreen(message: Component){
        conversation.add(
            Component.text("---------------------").color(NamedTextColor.WHITE)
                    + Component.text("[ Quest ]").color(NamedTextColor.GOLD)
                    + Component.text("---------------------\n").color(NamedTextColor.WHITE)
                    + message
                    + Component.text("\n----------------------------------------------------")
        )
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

