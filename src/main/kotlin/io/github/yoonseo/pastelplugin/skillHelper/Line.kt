package io.github.yoonseo.pastelplugin.skillHelper

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.distanceTo
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector

class Line private constructor(private val loc1 : Location, private val loc2: Location,dummy: Int = 1) {
    constructor(location1 : Location,location2: Location) : this(location1.clone(),location2.clone(),1)

    fun showParticle(particle: Particle, interval: Double){
        val slope = loc2.toVector().subtract(loc1.toVector()).normalize().multiply(interval)
        val times = ((loc1 distanceTo loc2)/slope.distance(Vector(0,0,0))).toInt()
        for(i in 0..times){
            loc1.add(slope)
            try {
                loc1.world.spawnParticle(particle,loc1,1,0.0,0.0,0.0,0.0)
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
    }
    fun showParticle(particle: Particle, interval: Double,data : Any){
        val slope = loc2.toVector().subtract(loc1.toVector()).normalize().multiply(interval)
        val times = ((loc1 distanceTo loc2)/slope.distance(Vector(0,0,0))).toInt()
        for(i in 0..times){
            loc1.add(slope)
            loc1.world.spawnParticle(particle,loc1,1,0.0,0.0,0.0,data)
        }
    }
}
infix fun Location.lineTo(location2 : Location) = Line(this,location2)