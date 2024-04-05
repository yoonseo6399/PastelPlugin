package io.github.yoonseo.pastelplugin.rpg

import io.github.yoonseo.pastelplugin.*
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.Location
import org.bukkit.entity.Player


class ConditionNode {
    var obtain: (Player.(Quest)->Boolean) = { true }
    var approachAt: (Player.(Quest)->Boolean) = { true }
    var kill: (Player.(Quest)->Boolean) = { true }
    fun obtain(){

    }
    fun approachAt(loc: Location,distance: Float){

    }
    fun kill(monster: Monster<*>,amount: Int){

    }
    val condition : Player.(Quest)->Boolean
        get() = { obtain(this,it) && approachAt(this,it) && kill(this,it) }
}

class ConversationNode{
    private val conversation = arrayListOf<Component>()
    private var optionalNode : OptionalNode? = null
    fun Npc.talk(message: Component){
        conversation.add(name + Component.text(" ") + message)
    }

    fun option(optionNode : OptionalNode.() -> Unit){
        optionalNode = OptionalNode()
        optionNode(optionalNode!!)
    }

    suspend fun print(player: Player){
        conversation.printTo(player)
        optionalNode?.optionPrintAndAwait(player)

    }
}
class OptionalNode {
    val options = hashMapOf<Component,ConversationNode>()
    var isChosen = false

    fun choice(choiceName: Component,node : ConversationNode.()->Unit){
        options[choiceName] = ConversationNode().also {node(it)}
    }

    suspend fun optionPrintAndAwait(player: Player){

        val optionalComponents = options.keys.map { it.clickEvent(ClickEvent.runCommand("/quest choose ${(it as? TextComponent)?.content()}")) }


        player.sendMessage(optionalComponents.mergeWithSeparator(Component.text(" ")))

        waitForCondition {
            isChosen
        }
    }

    suspend fun choose(player: Player, optionName : String){
        options.mapNotNull {
            entry -> (entry.key as? TextComponent)?.content()?.let { it to entry.value  }
        }.toMap()[optionName]?.print(player)
        isChosen = true
    }
}
data class Npc(val name : Component){

}
val a = Npc(Component.text("a"))
val c : ConversationNode.()->Unit = {
    a.talk(Component.text("a"))
    option {
        choice(Component.text("bb")){

        }
    }
}

