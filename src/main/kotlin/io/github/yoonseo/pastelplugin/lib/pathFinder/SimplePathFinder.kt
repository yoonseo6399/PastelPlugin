package io.github.yoonseo.pastelplugin.lib.pathFinder

import io.github.yoonseo.pastelplugin.distanceTo
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import java.util.TreeMap

typealias Node = Pair<Pair<Block,Block>,Double>

class SimplePathFinder(val entity : LivingEntity) {
    val openList = ArrayList<Node>()
    val closedList = ArrayList<Node>()
    fun findPathTo(x : Double,z : Double){
        //openList.add()



        listOf(
            entity.location.clone().add(1.0, 0.0, 0.0).block,
            entity.location.clone().add(0.0, 0.0, 1.0).block,
            entity.location.clone().add(1.0, 0.0, 1.0).block,
            entity.location.clone().add(-1.0, 0.0, 0.0).block,
            entity.location.clone().add(0.0, 0.0, -1.0).block,
            entity.location.clone().add(-1.0, 0.0, -1.0).block
        ).mapNotNull { if(it.isCollidable) null else entity.location.block to it to (it.midLocation distanceTo entity.location) }
        closedList.add(entity.location.block to entity.location.block to 0.0)
        openList.remove(openList.minBy { it.distance }.also { closedList.add(it) })


    }
    fun iterateNearBlocks() {

    }
}
val Node.parent : Block
    get() = first.first
val Node.child : Block
    get() = first.second
val Node.distance : Double
    get() = second

val Block.midLocation : Location
    get() = this.location.clone().add(0.5,1.0,0.5)

