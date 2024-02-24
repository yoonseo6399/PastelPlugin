package io.github.yoonseo.pastelplugin.robotArm

import io.github.yoonseo.pastelplugin.*
import io.github.yoonseo.pastelplugin.skillHelper.glowing
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import kotlin.math.roundToInt

open class ArmController
constructor(
    val owner: Player, val armLength : Double= 20.0, private val numberOfArm : Int = 6
) {
    var state = false
    val arms = ArrayList<Arm>(numberOfArm)

    val allocatedBlocks = ArrayList<Block>()

    var lastBodyLocation : Location = owner.location

    init {
        repeat(numberOfArm) {arms.add(Arm(armLength,owner.location,this))}// 팔 갯수만큼 Arms에 추가
        arms.forEach(::allocateBlockToArm)

        ScheduleRepeating {
            arms.forEach{
                it.moveBody(owner.location.add(0.0,1.0,0.0))
                it.showParticle()
            }
        }
        /*ScheduleRepeating(cycle = 5) {
            if(lastBodyLocation distanceTo owner.location > 5){
                arms.filter { it.handLocation distanceTo it.bodyLocation > 5}
                    .forEach(::allocateBlockToArm)
                lastBodyLocation = owner.location
            }
        }*/
    }

    fun allocateBlockToArm(arm : Arm){
        val nearBlocks = owner.location.nearByBlocks(armLength.toInt()){ block ->  !allocatedBlocks.any { it.location distanceTo block.location < 8  } }
        if(nearBlocks.isNotEmpty()){
            val nearestBlock = lowestObject(nearBlocks){ it.location distanceTo owner.location }
            allocatedBlocks.add(nearestBlock)
            debug( nearestBlock)
            arm.moveHand(nearestBlock.location.add(0.5,0.5,0.5))
        }

    }
    // Arm 에게 받는 Event
    internal fun disconnectedWithBlockEvent(arm : Arm,block: Block){
        block.glowing(false)
        allocatedBlocks.remove(block)
        allocateBlockToArm(arm)
    }

    fun on(){

    }
    fun off(){

    }
    private fun checkBlocksAndMoveArms(block: Block){

    }

}