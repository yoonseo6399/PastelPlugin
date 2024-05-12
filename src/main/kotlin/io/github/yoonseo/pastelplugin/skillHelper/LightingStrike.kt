package io.github.yoonseo.pastelplugin.skillHelper

import io.github.yoonseo.pastelplugin.randomDirection
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import java.lang.Math.random
import kotlin.time.times

class LightingStrike(val location : Location, val size: Double,var division : Int) {
    fun create(particle: Particle,direction: Vector,block : (Location) -> Unit = {}){
        val line = location lineTo location.add(direction.multiply(size + (-2..2).random()))
        line.showParticle(particle,0.1)
        block(location)
        if(division <= 0) return
        division--

        create(particle,direction.multiply(1.5).add(randomDirection(10).normalize()),block)
        if((random()*2).toInt() == 0) create(particle,direction.multiply(1.5).add(randomDirection(10).normalize()),block)

    }
}