package io.github.yoonseo.pastelplugin.rpg.moster

import io.github.yoonseo.pastelplugin.rpg.moster.impls.Goblin
import io.github.yoonseo.pastelplugin.rpg.moster.impls.Slime
import io.github.yoonseo.pastelplugin.rpg.moster.impls.Wolf
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

class MonsterCommand : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null){
            if(p3.size == 3){
                when(p3[1]){
                    "Slime" -> repeat(p3[2].toInt()){
                        Monster.spawn(Slime::class,(p0 as Player).location)
                    }
                    "Wolf" -> repeat(p3[2].toInt()){
                        Monster.spawn(Wolf::class,(p0 as Player).location)

                    }
                    "Goblin" -> repeat(p3[2].toInt()){
                        Monster.spawn(Goblin::class,(p0 as Player).location)
                    }
                }

            }
        }
        return true
    }

}