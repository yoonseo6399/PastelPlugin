package io.github.yoonseo.pastelplugin.robotArm

import HomingObject
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.distanceTo
import io.github.yoonseo.pastelplugin.skillHelper.MovingObject
import io.github.yoonseo.pastelplugin.skillHelper.lineTo
import org.bukkit.Location
import org.bukkit.Particle
import java.lang.RuntimeException

class Arm constructor(
    val length: Double,var bodyLocation : Location,val controller: ArmController
) {
    var handLocation : Location
    init {
        handLocation = bodyLocation
    }
    lateinit var handMotionLocation : Location
    val handMotioner = MovingObject(handLocation,1.0){
        handMotionLocation = it
    }

    fun moveHand(loc : Location){
        if(loc distanceTo bodyLocation > length)
        throw RuntimeException("location is too far away compared to decided length : $length vs ${loc distanceTo bodyLocation}\n $loc $bodyLocation")

        handLocation = loc

        handMotioner.moveTo(loc)
        //handMoving

    }
    fun showParticle(){(bodyLocation lineTo handMotionLocation).showParticle(Particle.BUBBLE_POP,0.5)}


    fun moveBody(loc : Location){
        bodyLocation = loc
        if(bodyLocation distanceTo handLocation > length){
            controller.disconnectedWithBlockEvent(this,handLocation.world.getBlockAt(handLocation))// 연결을 유지할수없음을 알리기
        }
    }

}