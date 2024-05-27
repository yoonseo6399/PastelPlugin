package io.github.yoonseo.pastelplugin.rpg.moster

import io.github.yoonseo.pastelplugin.rpg.moster.impls.Goblin
import io.github.yoonseo.pastelplugin.rpg.moster.impls.Slime
import io.github.yoonseo.pastelplugin.rpg.moster.impls.Wolf
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random
import kotlin.reflect.KClass

class MonsterCommand : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if(p3 != null){
            if(p3.size >= 3){
                var level = if(p3.size == 4) p3[3].toIntOrNull() ?: 1 else 1

                when(p3[1]){
                    "Slime" -> repeat(p3[2].toInt()){
                        Monster.spawn(Slime::class,(p0 as Player).location, level)
                    }
                    "Wolf" -> repeat(p3[2].toInt()){
                        Monster.spawn(Wolf::class,(p0 as Player).location, level)

                    }
                    "Goblin" -> repeat(p3[2].toInt()){
                        Monster.spawn(Goblin::class,(p0 as Player).location, level)
                    }
                    else -> throw Exception("monster not exist")
                }

            }
            if(p3.size == 1 && p3[0] == "autoSpawn") NeturalSpawnHandler()
        }
        return true
    }

}