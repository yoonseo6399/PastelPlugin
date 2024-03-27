package io.github.yoonseo.pastelplugin.rpg

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.CancelTask
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
typealias Conversation = List<Component>


class Quest {
    lateinit var questName : String
    lateinit var endAt : Player.(Quest) -> Boolean
    val subQuests = ArrayList<Quest>()
    var nextQuest : Quest? = null

    var questScreen : Component? = null
    var mustCompleteSubs = false

    var startConversation: Conversation? = null
    var endConversation: Conversation? = null

    var startRunCode : (Player.() -> Unit)? = null
    var endRunCode : (Player.() -> Unit)? = null

    var trackingTaskId = -1

    var isEnded = false

    lateinit var player : Player


    fun start(player: Player){
        this.player = player
        questScreen?.let { player.sendMessage("") ;player.sendMessage(it);player.sendMessage("") }
        startConversation?.printTo(player)
        startRunCode?.invoke(player)


        trackingTaskId = ScheduleRepeating {
            if(endAt(player,this)){
                if(mustCompleteSubs && subQuests.all { it.isEnded }) {
                    CancelTask(it)
                    end()
                }
                if(!mustCompleteSubs) {
                    CancelTask(it)
                    end()
                }
            }
        }
    }

    fun end(){
        HeartbeatScope().launch {
            endConversation?.printTo(player)?.join()
            endRunCode?.invoke(player)
            nextQuest?.start(player)

        }

    }

    fun interrupt(){
        CancelTask(trackingTaskId)
    }
    companion object{
        //DSL
        operator fun invoke(node : QuestNode.() -> Unit) : Quest{
            return Quest().also {
                node(QuestNode(it))
            }

        }
        val QuestLabel = Component.text("[ Quest ] ").color(NamedTextColor.GOLD)
    }
}
fun test(){
    Quest {

    }
}

fun Conversation.printTo(player: Player)=
    HeartbeatScope().launch {
        forEach {
            player.sendMessage(it)
            delay(1000L)
        }
    }


