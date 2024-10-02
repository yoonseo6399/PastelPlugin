package io.github.yoonseo.pastelplugin.vehicles

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.distanceTo
import io.github.yoonseo.pastelplugin.set
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import org.bukkit.Location
import org.bukkit.util.Vector


/**
 * @param vector Thrust's starting vector
 * @param maxPower ThrustLimit achieved by acceleration
 * @param acceleration tick per speed^2
 * @param speedLimit tick per block
 * @param location starting location
 * @param gravityForce gravity tick per downforce( block )
 * */
abstract class ThrustBasedUnit(
    val vector: Vector,
    val maxPower : Double = 0.0,
    val acceleration: Double = 1.0,
    val speedLimit : Double,
    override var location : Location,
    val gravityForce : Double,
    override val parts : ArrayList<UnitPart> = ArrayList()
) : Unit(location){
    var thrust = 0.0
    var isDeleted = false
    val velocity = Vector()
    /**adjust altitude() function's return valve by simply add it*/
    open val altitudeModifier : Double = 0.0
    init {
        ScheduleRepeating(until = {isDeleted}) { nextPos();update() }
    }
    /**delete unit*/
    fun delete(){
        isDeleted = true
    }
    /**this function is called every tick
     * @return returns next Unit's pos*/
    fun nextPos() : Location{


        //thrust and gravity calculation
        velocity.add(vector.multiply(thrust).add(Vector(0,-1,0).multiply(gravityForce)))


        if(velocity.length() >= speedLimit) { //쓰러스트로 인한 과속방지 ( Y axis include )
            val counterVector = velocity.clone().multiply(speedLimit).subtract(velocity) //velocity에 더할시 speedlimit이내로 들어오게 강제 조절
            velocity.add(counterVector)
        }

        //착륙판정 && 땅 판정
        if(altitude() <=1 && velocity.y <= 0){ //땅에 가까우면
            //계산한 velocity   와 충돌하는 블럭이 있다면, forceLand
            Selector(location distanceTo location.clone().add(velocity)).selectBlock(location)?.location?.add(0.0,altitudeModifier,0.0)?.let {
                location.set(it)
                velocity.setY(0)
                frictionBrake()
            }
        }
        //반영
        location.add(velocity)
        return location
    }
    /**this forces unit land on ground instantaneously ( with considering x,y velocity )
     * but if altitude is over 100, this function doesn't work properly*/
    fun forceLand(){
        val landingPos = Selector(100).selectBlock(location, direction = velocity.clone())?.location?.add(0.0,altitudeModifier,0.0)
        landingPos?.let { location.set(it) }
    }

    private fun frictionBrake(){
        velocity.multiply(0.65)
    }

    abstract fun update()


    open fun isOnGround() : Boolean{
        return altitude() == 0.0
    }
    open fun altitude() : Double {
        val block = Selector(100).selectBlock(location, direction = Vector(0,-1,0)) ?: return 101.0
        return location.y - block.y + altitudeModifier
    }


    fun accelerate(){ thrust += acceleration }
    fun decelerate(){ thrust -= acceleration/1.2}


}