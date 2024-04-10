package io.github.yoonseo.pastelplugin.rpg.quest
////////io.github.yoonseo.pastelplugin.rpg.quest.QuestCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import io.github.yoonseo.pastelplugin.rpg.quest.OptionalNode
import io.github.yoonseo.pastelplugin.rpg.quest.impl.Quests
import org.bukkit.Bukkit
import kotlin.reflect.full.functions

class QuestCommand : CommandExecutor {
    companion object{
        private val optionalQuest = hashMapOf<Player, OptionalNode>()
        fun addOptionalQuest(player: Player,optionalNode: OptionalNode){
            optionalQuest[player] = optionalNode
        }
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null){
            if(p3.size == 2 && p3[0] == "choose" && p0 is Player && p3[1].toIntOrNull() != null){
                if (optionalQuest[p0]?.choose(p3[1].toInt()) == true) {
                    optionalQuest.remove(p0)
                }
            }
            if(p3.size == 3 && p3[0] == "apply"){
                if(Bukkit.getPlayer(p3[1]) != null){
                    Quests::class.functions.firstOrNull { it.name == p3[2] }?.call(Quests)?.let {
                        (it as Quest).start(Bukkit.getPlayer(p3[1])!!)
                        p0.sendMessage("quest ${p3[2]} applied to ${Bukkit.getPlayer(p3[1])!!.name}")
                    }
                }
            }
        }
        return true
    }
}