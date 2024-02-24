package io.github.yoonseo.pastelplugin.skillHelper

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.distanceTo
import org.bukkit.Location

class MovingObject(val location: Location,val speed: Double,val everyMovement: (Location)->Unit) {



    fun moveTo(other: Location){
        val slope = other.toVector().subtract(location.toVector()).normalize().multiply(speed) // location to other slope

        ScheduleRepeating(taskAllocator = this, until = {location distanceTo other <= speed}) {

            location.add(slope)
            everyMovement(location)
        }
    }
}