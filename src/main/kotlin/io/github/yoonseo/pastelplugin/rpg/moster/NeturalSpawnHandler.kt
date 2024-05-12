package io.github.yoonseo.pastelplugin.rpg.moster

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.getOverworldLocation
import io.github.yoonseo.pastelplugin.rpg.moster.impls.Slime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import java.lang.Math.random

class NeturalSpawnHandler {
    val list = listOf<Pair<Location,(Location) -> Unit>>(
        getOverworldLocation(-582,36,29201) to {
                                               Monster.spawn(Slime::class,it,(random() * 5).toInt())
        },

    )
    val range = 20
    init {
        HeartbeatScope().launch {
            while (true){
                list.forEach {
                    if(it.first.getNearbyLivingEntities(range.toDouble()).size >= 10*range) return@forEach
                    it.second(it.first.world.getHighestBlockAt(it.first.clone().add(random()*range, 0.0, random()* range)).location)
                }
                delay(20000)
            }

        }
    }
}