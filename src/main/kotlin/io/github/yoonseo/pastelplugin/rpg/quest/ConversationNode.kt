package io.github.yoonseo.pastelplugin.rpg.quest


import io.github.yoonseo.pastelplugin.plus
import io.github.yoonseo.pastelplugin.toComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player


class ConversationNode{
    private val conversation = arrayListOf<Component>()
    private var optionalNode : OptionalNode? = null
    private var executes : ExecutableNode? = null
    fun Npc.talk(message: Component){
        conversation.add(Component.text("[ ") + name + Component.text(" ] ") + message)
    }
    fun Npc.talk(message: String){
        talk(message.toComponent())
    }
    fun tell(message: Component){
        conversation.add(message)
    }
    fun tell(message: String){
        tell(message.toComponent())
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

