package io.github.yoonseo.pastelplugin.commands

import io.github.yoonseo.pastelplugin.rpg.ConversationNode
import io.github.yoonseo.pastelplugin.rpg.OptionalNode
import kotlinx.coroutines.delay
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object QuestCommand : CommandExecutor {
    val optionalQuest = hashMapOf<Player,List<String>>()
    val optionalQuestInstance = hashMapOf<Player,OptionalNode>()

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null){
            if(p3.size == 2 && p3[0] == "choose"){
                optionalQuest[p0]?.firstOrNull { it == p3[1] }?.let {  }
            }
        }
        return true
    }

    suspend fun addOptionalQuestCallback(){



    }

}