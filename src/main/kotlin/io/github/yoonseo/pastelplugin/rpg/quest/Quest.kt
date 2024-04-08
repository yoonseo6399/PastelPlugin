package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.plus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import io.github.yoonseo.pastelplugin.rpg.quest.*

typealias Conversation = List<Component>


class Quest {
    lateinit var player : Player

    val sessions = ArrayList<QuestSession>()
    companion object;

    lateinit var questName : Component
    fun showQuestScreen(){
        player.sendMessage(Component.text("----------------------").color(NamedTextColor.WHITE)+Component.text("[ Quest ]").color(NamedTextColor.GOLD)+Component.text("----------------------").color(NamedTextColor.WHITE))
        player.sendMessage("")
        player.sendMessage(questName)
        player.sendMessage("","----------------------------------------------------")
    }

    fun start(player: Player){
        this.player = player
        HeartbeatScope().launch {
            while (true){
                sessions.minBy { it.session.order }.startSession(player)
                if(sessions.minOf { it.session.order } == Session.End.order){
                    break
                }
            }

        }
    }
}

fun Quest.Companion.create(questName : Component,node : RootQuestingNode.() -> Unit) = Quest().also { it.questName = questName;node(RootQuestingNode(it)) }

suspend fun Conversation.printTo(player: Player)=
        forEach {
            player.sendMessage(it)
            delay(1000L)
        }



