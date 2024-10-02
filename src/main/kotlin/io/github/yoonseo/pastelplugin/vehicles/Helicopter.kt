package io.github.yoonseo.pastelplugin.vehicles

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.display.visualize
import io.github.yoonseo.pastelplugin.lib.adjustableValue
import io.github.yoonseo.pastelplugin.spawn
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.acos

class Helicopter(override var location : Location,val rider : LivingEntity?) : ThrustBasedUnit(
    Vector(0,1,0),
    1.0,
    0.2,
    10.0,
    location,
    0.92,
    arrayListOf(RouterBlade())
)
{
    init {
        load()
    }
    val routerBlade = parts.find { it.name == "RouterBlade" }!!
    var angle : Float by adjustableValue("angle",0.0f) // Deg


    override fun update() {
        if(rider != null){
            val lastTilt = rotation
            quaternionUpdate()
            val diffQ = Quaternionf(lastTilt).invert().mul(rotation).normalize()

            //rider.location.direction.let { rotation.set(it.x,it.y,it.z,angle).normalize() ;   }

            val updater = PartUpdate(location.clone(), isDeparted = false, tilting = rotation, updateRotationQuaternionf = Quaternionf(diffQ))
            //routerBlade.update(updater)
            update(updater)
        }
        //TODO Accelerate / Decelerate Implement

    }
    private fun quaternionUpdate(){
        rider ?: return

        val yAxisR = Quaternionf().fromAxisAngleDeg(Vector3f(0.0f,1.0f,0.0f),-rider.yaw)
        val pitchAxisR = Quaternionf().fromAxisAngleDeg(Vector3f(1.0f,0.0f,0.0f),rider.pitch)

        val dirQ = yAxisR.mul(pitchAxisR).normalize()
        val tilt = Quaternionf().fromAxisAngleDeg(Vector3f(0.0f,0.0f,1.0f),angle)
        rotation = dirQ.mul(tilt).normalize()
        rotation.visualize(rider.eyeLocation)
    }


    override fun load() {
        mainObject = location.spawn(BlockDisplay::class).apply {
            addPassenger(location.spawn(BlockDisplay::class).apply {
                block = Material.RED_TERRACOTTA.createBlockData()
                transformation = Transformation(Vector3f(), Quaternionf(), Vector3f(2f,0.0625f,0.0625f), Quaternionf())
            })
            addPassenger(location.spawn(BlockDisplay::class).apply {
                block = Material.GREEN_TERRACOTTA.createBlockData()
                transformation = Transformation(Vector3f(), Quaternionf(), Vector3f(0.0625f,2f,0.0625f),Quaternionf())
            })
            addPassenger(location.spawn(BlockDisplay::class).apply {
                block = Material.BLUE_TERRACOTTA.createBlockData()
                transformation = Transformation(Vector3f(), Quaternionf(), Vector3f(0.0625f,0.0625f,2f),Quaternionf())
            })
        }
    }
    override fun update(data: PartUpdate) {
        mainObject.passengers.forEach {
            if(it is BlockDisplay){
                it.transformation = Transformation(
                    it.transformation.translation,
                    it.transformation.leftRotation,
                    it.transformation.scale,
                    data.updateRotationQuaternionf.mul(it.transformation.rightRotation).normalize().also { debug(it) }
                )
            }
        }
    }


}
class RouterBlade() : UnitPart(Vector(0,3,0),"RouterBlade"){
    lateinit var objects : ArrayList<Display>
    lateinit var main : Display
    var rotation : Double = 0.0
    var rotationAxis : Vector = Vector(0,1,0)
    override fun load() {
        TODO("Not yet implemented")
    }

    override fun update(data: PartUpdate) {
        rotationAxis = data.localYAxis
        //val tilted = data.localYAxis.clone().rotateAroundAxis(rotationAxis,rotation)
        //objects.forEach {
        //    it.location.setDirection(tilted)
        //}
        data.relativePos?.let { relativePos = it }
        //main.transformation.leftRotation
        //val newLoc = data.centerBodyLocation.add(data.tilting.transform(relativePos.toJoml3d()).toBukkit())
        //main.location.set(newLoc)
    }
}

infix fun Vector.Quaternionf(angle: Double) = Quaternionf(x,y,z,angle)
fun Quaternionf.getVector() = Vector(x,y,z)