package io.github.yoonseo.pastelplugin.rpg

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.CancelTask
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.plus
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
typealias Conversation = List<Component>


class Quest {
    lateinit var player : Player
    var startCondition : (Player.(Quest)->Boolean) = { true }
    var startConversation : ConversationNode? = null
    //val endCondition : null
//
    //var prograssionBar = null

    lateinit var questName : Component
    fun showQuestScreen(){
        player.sendMessage(Component.text("----------------------").color(NamedTextColor.WHITE)+Component.text("[ Quest ]").color(NamedTextColor.GOLD)+Component.text("----------------------").color(NamedTextColor.WHITE))
        player.sendMessage("")
        player.sendMessage(questName)
        player.sendMessage("")
        player.sendMessage("----------------------------------------------------")
    }
    private suspend fun waitStartCondition(){
        while (true){
            if(startCondition(player,this)) break
            delay(500L)
        }
    }

    fun start(player: Player){
        this.player = player
        HeartbeatScope().launch {
            waitStartCondition()

            startConversation?.print(player)
        }


    }
}


suspend fun Conversation.printTo(player: Player)=
        forEach {
            player.sendMessage(it)
            delay(1000L)
        }



