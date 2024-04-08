package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.approachAt
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class RootQuestingNode(val quest: Quest) {
    fun Session.open(node: QuestingNode.() -> Unit){
        val session = QuestSession(quest,this)
        node(QuestingNode(session))
        quest.sessions.add(session)
    }
}
class QuestingNode(private val session: QuestSession) {
    fun condition(conditionNode: ConditionNode.() -> Unit){
        session.conditionNode = ConditionNode().also { conditionNode(it) }
    }
    fun conversation(conversationNode: ConversationNode.() -> Unit){
        session.conversationNode = ConversationNode().also { conversationNode(it) }
    }
    fun executes(executableNode: ExecutableNode.() -> Unit){
        session.executableNode = ExecutableNode(session).also { executableNode(it) }
    }
}
class ExecutableNode(val session : QuestSession){
    var code : (() -> Unit)?= null
    var giveItemCode : (() -> Unit)?= null
    fun code(code : () -> Unit){
        this.code = code
    }
    fun giveItem(item: ItemStack){
        giveItemCode = {
            session.quest.player.inventory.addItem(item)
        }
    }
    operator fun invoke(){
        code?.invoke()
        giveItemCode?.invoke()
    }
}

class ConditionNode {
    var obtain: (Player.(Quest)->Boolean) = { true }
    var approachAt: (Player.(Quest)->Boolean) = { true }
    var kill: (Player.(Quest)->Boolean) = { true }
    fun obtain(item : ItemStack,amount: Int){
        obtain = {
            inventory.containsAtLeast(item,amount)
        }
    }
    fun approachAt(loc: Location,distance: Float){
        approachAt = {
            approachAt(loc,distance)
        }
    }
    fun kill(monster: Monster<*>,amount: Int){
        TODO()
    }
    val condition : Player.(Quest)->Boolean
        get() = { obtain(this,it) && approachAt(this,it) && kill(this,it) }
    operator fun invoke(player: Player,quest: Quest) = condition(player,quest)
}


data class Npc(val name : Component){

}


