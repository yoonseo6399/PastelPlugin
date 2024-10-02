package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.skillHelper.lineTo
import io.github.yoonseo.pastelplugin.vehicles.getVector
import io.github.yoonseo.pastelplugin.vehicles.toBukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f

fun Vector.visualize(loc : Location,particle: Particle = Particle.SCULK_CHARGE_POP) {
        val line = if(isNormalized) loc lineTo loc.clone().add(this.clone().multiply(3)) else loc.clone() lineTo loc.add(this.clone())

        line.showParticle(particle,0.5)
}
fun Quaternionf.visualize(loc: Location){
    getVector().normalize().visualize(loc)
    //debug("visualizing Vector : ${getVector()}")
    transform(Vector3f(1.0f,0.0f,0.0f)).toBukkit().visualize(loc,Particle.BUBBLE_POP)
    transform(Vector3f(0.0f,1.0f,0.0f)).toBukkit().visualize(loc,Particle.CRIT)
    transform(Vector3f(0.0f,0.0f,1.0f)).toBukkit().visualize(loc,Particle.COMPOSTER)
}