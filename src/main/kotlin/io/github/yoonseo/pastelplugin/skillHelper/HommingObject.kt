
import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.debug
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.util.Vector
import kotlin.math.sqrt


class HomingObject(val location: Location, var targetEntity: Entity, val shooter: LivingEntity) {
    constructor(location: Location, targetEntity: Entity, shooter: LivingEntity, init: HomingObject.() -> Unit): this(location,targetEntity,shooter){
        init(this)
    }

    var launched = false
    var acceleration = 0.5f
    var currentSpeed = 0.0f
    var startingSpeed = 2.0f
    var maxSpeed = 20.0f
    var whenCollusion : (HomingObject.(LivingEntity?) -> Unit)? = null
    var everyMovement : (HomingObject.() -> Unit)? = null
    var cancelWhenTargetIsntExist = false
    var rotationLimit = 1.0f
    var homingDirection: Vector


    var taskId: Int = -1
    init{
        homingDirection = location.direction
    }

    fun launch(plugin: JavaPlugin = PastelPlugin.plugin){
        if(launched) throw IllegalStateException("this object is already launched")
        launched = true
        currentSpeed = startingSpeed




        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,0){ //유도
            homingDirection.multiply(0.25)
            repeat(currentSpeed.toInt()){
                //debug("")
                //debug("repeated $it")
                //debug("homing direction : $homingDirection ,location : ${location.toString()}")
                val homingLocationTemp = location.clone().add(homingDirection.multiply(currentSpeed).multiply(rotationLimit))//.also { debug("homingLocTemp : $it") }
                val homingDirectionTemp = targetEntity.location.add(0.0,1.0,0.0).toVector().subtract(homingLocationTemp.toVector()).normalize()//.also { debug("homingdirtenp : $it") } // 몇칸 앞에서 계산
                val homingLocation = homingLocationTemp.clone().add(homingDirectionTemp.multiply(0.125))//.also { debug("homingLoc : $it") }
                homingDirection = homingLocation.toVector().subtract(location.toVector()).normalize()




                location.add(homingDirection.multiply(0.125))//.also { debug("a" + it) })

                //추진


                //감속 + 공기저항
                currentSpeed -= ((getDistance(homingLocationTemp.clone().add(homingDirection.multiply(1)),homingLocation).toFloat()-0.51f)*currentSpeed*(acceleration/maxSpeed))
                if(currentSpeed < startingSpeed){
                    currentSpeed = startingSpeed
                }

                getEntityColludeWithLocation(location).mapNotNull { it as? LivingEntity }
                val radius = 1.0
                val entities = location.world.getNearbyEntities(location, radius,radius,radius){ it is LivingEntity }.map {it as LivingEntity}
                if(location.block.isCollidable || entities.isNotEmpty()){
                    explode(entities.firstOrNull())
                }

                //if(!location.world.getNearbyEntities(location,1.0,1.0,1.0).isEmpty()) explode()
                everyMovement?.let { it1 -> it1(this) }

            }


            // A - - - - B

            if(maxSpeed/currentSpeed * acceleration+currentSpeed > maxSpeed){
                currentSpeed = maxSpeed
            }else currentSpeed += maxSpeed/currentSpeed * acceleration


            //감속


            if(targetEntity.isDead && cancelWhenTargetIsntExist) Bukkit.getScheduler().cancelTask(taskId)
        }
    }

    private fun explode(entity: LivingEntity?){
        whenCollusion?.let { it(this,entity) }
    }
    fun kill(){
        Bukkit.getScheduler().cancelTask(taskId)
    }
}

fun BukkitScheduler.scheduleSyncRepeatingTask(plugin: JavaPlugin, period: Long, action: Runnable) :Int {
    return this.scheduleSyncRepeatingTask(plugin,action,0L,period)
}
fun getDistance(loc1: Location, loc2: Location): Double {
    val dx = loc1.x - loc2.x
    val dy = loc1.y - loc2.y
    val dz = loc1.z - loc2.z
    return sqrt(dx * dx + dy * dy + dz * dz)
}

fun getEntityColludeWithLocation(loc: Location): List<Entity>{
    val list = ArrayList<Entity>()
    for(entity in loc.world.entities){
        if(entity.boundingBox.overlaps(loc.toVector(),loc.toVector())) list + entity
    }
    return list
}

data class HomingVector(val vector: Vector, var speed: Float)