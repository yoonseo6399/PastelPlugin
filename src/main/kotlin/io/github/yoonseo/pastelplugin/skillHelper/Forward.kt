package io.github.yoonseo.pastelplugin.skillHelper

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

object Forward {
    fun run(startLocation: Location,direction: Vector,range: Double,interval: Double = 1.0,oneBlockDelay: Long = 50,tickRepeating : Int = 1,block : suspend CoroutineScope.(Location) -> Unit){
        HeartbeatScope().launch {
            val loc = startLocation.clone();val dir = direction.multiply(interval)
            repeat((range).toInt()){
                repeat((1*tickRepeating/interval).toInt()){
                    loc.add(dir)
                    block(this,loc)
                }
                delay(oneBlockDelay)
            }
        }
    }
    inline fun <reified T : Entity>runWithEntityCollusion(
        startLocation: Location,
        direction: Vector,
        range: Double,
        interval: Double = 1.0,
        oneBlockDelay: Long = 50,
        crossinline collusionPredicate : (T) -> Boolean = {true},
        tickRepeating : Int = 1,
        crossinline block : (Location, List<T>) -> Unit)
    {
        run(startLocation,direction,range,interval,oneBlockDelay,tickRepeating){loc ->
            startLocation.world.getNearbyEntities(loc,0.2,0.2,0.2){ (it as? T)?.let(collusionPredicate) == true }.mapNotNull { it as? T }.let {
                block(loc,it)
            }
        }
    }
    inline fun <reified T : Block>runWithBlockCollusion(
        startLocation: Location,
        direction: Vector,
        range: Double,
        interval: Double = 1.0,
        oneBlockDelay: Long = 50,
        crossinline collusionPredicate : (T) -> Boolean = {true},
        tickRepeating : Int = 1,
        crossinline block : (Location, T?) -> Unit)
    {
        run(startLocation,direction,range,interval,oneBlockDelay,tickRepeating){loc ->
            val cblock = startLocation.world.getBlockAt(loc).takeIf { it is T && collusionPredicate(it) } as? T
            block(loc,cblock)
        }
    }

}