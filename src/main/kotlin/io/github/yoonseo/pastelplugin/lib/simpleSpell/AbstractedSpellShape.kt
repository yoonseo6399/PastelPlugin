package io.github.yoonseo.pastelplugin.lib.simpleSpell

import getEntityColludeWithLocation
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.getOverworldLocation
import io.github.yoonseo.pastelplugin.intoOneList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.PI
interface SpellShape{
    var point1 : Location
    var point2 : Location?
    var point3 : Location?
    var range : Double?
    fun <T : Any> runByLocationInShape(period: Double, block: (Location) -> T) : List<T>

    fun showParticle(period: Double,particleProvider : (Location) -> Unit)

    fun collidedEntity(period: Double) : List<Entity>
}



abstract class AbstractedSpellShape : SpellShape{
    abstract override var point1 : Location
    override var point2 : Location? = null
    override var point3 : Location? = null
    override var range : Double? = null
    abstract override fun <T : Any> runByLocationInShape(period: Double, block: (Location) -> T) : List<T>

    override fun showParticle(period: Double, particleProvider : (Location) -> Unit){
        runByLocationInShape(period,particleProvider)
    }

    override fun collidedEntity(period: Double) = runByLocationInShape(period) { getEntityColludeWithLocation(it) }.intoOneList()
}

class Circle(center : Location, val rotatedByAxis : Vector? = null, range : Double) : AbstractedSpellShape(){
    companion object {
        infix fun centered(loc : Location) = CircleBuilder(loc,-1.0,null)
    }

    override var point1: Location = center
    override var range : Double? = range

    override fun <T : Any> runByLocationInShape(period: Double, block: (Location) -> T) : List<T>{
        val circleVec = if(rotatedByAxis != null) Vector(rotatedByAxis.z,rotatedByAxis.x,-rotatedByAxis.y) else Vector(1,0,0)
        val axis = rotatedByAxis ?: Vector(0,1,0)
        circleVec.multiply(range!!)
        val repetition = (range!! * 2 * PI) / period //

        val list = ArrayList<T>()

        for (i in 0..repetition.toInt()){
            circleVec.rotateAroundAxis(axis,period)
            val result = block(point1.clone().add(circleVec))
            list.add(result)
        }
        return list
    }

}
data class CircleBuilder(var center : Location, var range : Double, var rotatedByAxis: Vector?){
    infix fun rotatedByAxis(vector: Vector) : CircleBuilder {
        rotatedByAxis = vector
        return this
    }

    infix fun makeByRadius(radius : Double) : Circle{
        range = radius
        return Circle(center,rotatedByAxis,range)
    }
}
class SpellShapeWithBehavior<S : AbstractedSpellShape>(val behavior: Behavior<S,Unit>, val shape : S) : SpellShape by shape {
    fun activateDelta(callBack: (S) -> Unit){
        behavior.runByCallBack(shape){
            callBack(shape)
        }
    }
}
class Behavior<T : Any,E : Any>(val block: suspend CoroutineScope.(T) -> E,val times: Int){
    private var isActive = false
    fun runByCallBack(value : T,callBack : (T) -> Unit){
        if(isActive) return
        var time = 0
        HeartbeatScope().launch {
            while (true){
                time ++
                block(this,value)
                callBack(value)
                if(time >= times) break
                delay(50)
            }
        }
    }
}

infix fun <T : AbstractedSpellShape> T.addGradual(block: suspend CoroutineScope.(T) -> Unit) : Pair<T,suspend CoroutineScope.(T) -> Unit>{
    return this to block
}
infix fun <T : AbstractedSpellShape> Pair<T,suspend CoroutineScope.(T) -> Unit>.times(times: Int) =
    SpellShapeWithBehavior(Behavior(second,times),first)



fun tes() {
    val s = Circle centered getOverworldLocation(0,0,0) makeByRadius 10.0 addGradual { it.range = it.range!! + 1 } times 10 // gradual에 대한 겟함수 변경
    s.activateDelta {  }
}
