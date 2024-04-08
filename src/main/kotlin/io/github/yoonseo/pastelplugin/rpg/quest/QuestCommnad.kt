package io.github.yoonseo.pastelplugin.rpg.quest

import quest.OptionalNode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class QuestCommand : CommandExecutor {
    companion object{
        private val optionalQuest = hashMapOf<Player, OptionalNode>()
        fun addOptionalQuest(player: Player,optionalNode: OptionalNode){
            optionalQuest[player] = optionalNode
        }
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null){
            if(p3.size == 2 && p3[0] == "choose" && p0 is Player){
                optionalQuest[p0]?.choose(p3[1])
            }
        }
        return true
    }


}