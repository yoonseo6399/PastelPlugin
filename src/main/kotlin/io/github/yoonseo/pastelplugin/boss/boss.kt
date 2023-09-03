package io.github.yoonseo.pastelplugin.boss

import io.github.monun.kommand.KommandContext
import io.github.monun.kommand.kommand
import io.github.yoonseo.pastelplugin.PastelPlugin
import org.bukkit.Location
import java.util.EnumSet

class bossCommand {
    init{
        PastelPlugin.plugin.kommand {
            register("boss"){
                then("spawn"){
                    then("bossName" to string()){
                        executes {
                            if(!isPlayer) {
                                sender.sendMessage("player only command")
                                return@executes
                            }
                            Boss.spawn(it["bossName"],player.location)
                        }
                    }
                }
            }
        }
    }
}

abstract class Boss(bossName : String){
    companion object{
        val list = HashMap<String,Boss>()
        fun spawn(bossName: String,loc: Location): Boss? {
            return list[bossName]?.spawn(loc)
        }
    }
    init{
        list[bossName] = this
    }

    abstract fun spawn(loc : Location) : Boss

}
