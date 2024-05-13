package io.github.yoonseo.pastelplugin.skillHelper

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.randomDirection
import io.github.yoonseo.pastelplugin.simpleString
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import java.lang.Math.random
import kotlin.math.absoluteValue
import kotlin.time.times

class LightingStrike(val location : Location, val size: Double,var division : Int) {
    fun create(particle: Particle,direction: Vector,options: Any? = null,block : (Location) -> Unit = {}){
        val line = location.clone() lineTo location.add(direction.clone().multiply((size * random()-1))).clone()
        if(options != null){
            line.showParticle(particle,0.2, options)
        }else line.showParticle(particle,0.2)
        block(location)
        if(division <= 0) return
        division--

        val nextDIR = direction.clone().multiply(1.05).add(randomDirection(10).normalize())
        //debug("INFO : Location : ${location.simpleString}, NEXT: $nextDIR, size : $size,division : $division")
        if((random()*2).toInt() == 0) LightingStrike(location.clone().also { debug(it) },size,division)
            .create(particle,direction.clone().multiply(1.05).add(randomDirection(10).normalize()),options,block)
        create(particle,nextDIR,options,block)

    }
}