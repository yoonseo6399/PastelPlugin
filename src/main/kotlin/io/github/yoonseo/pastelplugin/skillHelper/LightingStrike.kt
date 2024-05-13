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
    fun create(particle: Particle,direction: Vector,block : (Location) -> Unit = {}){
        val line = location.clone() lineTo location.add(direction.clone().multiply((size * random()+1).also { debug(it) })).clone()
        line.showParticle(particle,0.1)
        block(location)
        if(division <= 0) return
        division--

        val nextDIR = direction.clone().multiply(1.2).add(randomDirection(10).normalize())
        debug("INFO : Location : ${location.simpleString}, NEXT: $nextDIR, size : $size,division : $division")
        create(particle,nextDIR,block)
        if((random()*2).toInt().also { debug(it) } == 0) create(particle,direction.clone().multiply(1.2).add(randomDirection(10).normalize()),block)

    }
}