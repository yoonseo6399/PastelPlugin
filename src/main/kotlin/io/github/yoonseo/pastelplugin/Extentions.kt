@file:Suppress("FunctionName")

package io.github.yoonseo.pastelplugin


import getDistance
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.reflect.KClass


infix fun ItemStack.isNamed(name : String) =
    (this.itemMeta?.displayName() as? TextComponent)?.content() == name
infix fun ItemStack.isntNamed(name : String) =
    (this.itemMeta?.displayName() as? TextComponent)?.content() != name


internal const val ALLOCATOR = """ALLOT_OB:"""

fun ScheduleRepeating(cycle: Long = 1,delay: Long = 0,expireTick: Long = -1,taskName: String = "",until: (() -> Boolean)? = null,task: (Int) -> Unit): Int {
    val plugin = PastelPlugin.plugin
    if(taskName != "" && PastelPlugin.taskList.contains(taskName)) CancelTask(taskName)

    var id = -1



    id = if(until != null){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            task(id)
            if (until()) {
                debug("task cancelled by until. $taskName")
                if(taskName != "") CancelTask(taskName) else CancelTask(id)
            }
        }, delay, cycle)
    }else {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,{
            task(id)
        },delay,cycle)
    }

    if(expireTick > 0 && until == null){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,{ Bukkit.getScheduler().cancelTask(id);PastelPlugin.taskList.remove(taskName) },expireTick)
    }
    if(taskName != "") PastelPlugin.taskList[taskName] = id
    return id
}
fun ScheduleRepeating(cycle: Long = 1,delay: Long = 0,expireTick: Long = -1,taskAllocator: Any,until: (() -> Boolean)? = null,task: (Int) -> Unit) =
    ScheduleRepeating(cycle,delay,expireTick, ALLOCATOR+taskAllocator,until,task)





fun CancelTask(id: Int) {
    Bukkit.getScheduler().cancelTask(id)
}
fun CancelTask(taskName: String){
    PastelPlugin.taskList[taskName]?.let { Bukkit.getScheduler().cancelTask(it);PastelPlugin.taskList.remove(taskName) }
}

fun List<Component>.mergeWithSeparator(separator : Component) : Component{
    var component = Component.empty()
    forEach {
        component = component.append(it).append(separator)
    }
    return component
}

suspend inline fun waitForCondition(condition: () -> Boolean){
    while (true){
        if(condition()) break
        delay(500L)
    }
}

/**allocator only*/
fun CancelTask(taskAllocator: Any){
    CancelTask(ALLOCATOR+taskAllocator)
}


fun Delay(ticks: Long,task: Runnable): Int{
    return Bukkit.getScheduler().scheduleSyncDelayedTask(PastelPlugin.plugin,task,ticks)
}
/**searching nearestBlock in square shape**/
fun Location.nearByBlocks(searchDistance : Int ,condition : (Block)->Boolean = {true}) : List<Block> {
    val startingPoint = clone().add((0-searchDistance/2).toDouble(), (0-searchDistance/2).toDouble(),
        (0-searchDistance/2).toDouble()
    )
    val blocks = ArrayList<Block>()
    repeat(searchDistance){dx->
        repeat(searchDistance){dy->
            repeat(searchDistance){dz->
                val block = world.getBlockAt(startingPoint.blockX+dx,startingPoint.blockY+dy,startingPoint.blockZ+dz)

                if(!block.isEmpty && condition(block)){
                    blocks.add(block)
                }
            }
        }
    }
    return blocks
}

fun <T : Any> lowestObject(list :List<T>,transform : (T) -> Double) : T{
    require(list.isNotEmpty())


    var lowest = list.first() to transform(list.first())
    for(some in list){
        if(lowest.second > transform(some)) lowest = some to transform(some)
    }
    return lowest.first
}
fun <T : Any> biggestObject(list :List<T>,transform : (T) -> Double) : T{
    require(list.isNotEmpty())


    var biggest = list.first() to transform(list.first())
    for(some in list){
        if(biggest.second > transform(some)) biggest = some to transform(some)
    }
    return biggest.first
}




infix fun Location.distanceTo(loc : Location) = getDistance(this,loc)

fun Mob.getDistanceFromTarget() : Double? = getCustomTargets().firstOrNull()?.let { it.location distanceTo location }

fun Mob.getCustomTargets() : List<LivingEntity> {
    return location.world.getNearbyEntities(location,50.0,50.0,50.0) { it is LivingEntity && it != this }.map { it as LivingEntity }
}
fun Mob.isTargetInRange(range: Double) = getDistanceFromTarget()?.let { it <= range } == true

val Mob.customTarget
    get() = getCustomTargets().firstOrNull()

infix fun Vector.seeVectorTo(vector: Vector): Vector{
    return subtract(vector).normalize().multiply(-1)
}
infix fun Vector.seeVectorTo(vector: Location): Vector{
    return subtract(vector.toVector()).normalize().multiply(-1)
}
infix fun Location.seeVectorTo(vector: Location): Vector{
    return toVector().subtract(vector.toVector()).normalize().multiply(-1)
}
infix fun Location.seeVectorTo(vector: Vector): Vector{
    return toVector().subtract(vector).normalize().multiply(-1)
}

fun Player.sendDebugMessage(msg: Any){
    if(displayName().toText() == "command_juho") sendMessage(Component.text(msg.toString()))
}

fun debug(msg : Any?){
    command_juho()?.sendMessage((msg ?: "null").toString())
}

fun Component.toText() =
    (this as TextComponent).content()

operator fun Component.plus(c : Component) = append(c)

fun Player.approachAt(loc : Location,distance : Float) =
    location distanceTo loc <= distance

fun getOverworldLocation(x : Int,y : Int,z : Int): Location = Location(overworld,x.toDouble(),y.toDouble(),z.toDouble())
fun getOverworldLocation(x : Double,y : Double,z : Double): Location = Location(overworld, x, y, z)

fun String.toComponent() = Component.text(this)

infix fun ItemStack.ComponentNamedWith(textComponent: TextComponent): Boolean {
    val com1 = this.itemMeta?.displayName() ?: return false
    if(com1 is TranslatableComponent){
        return com1.args().firstNotNullOfOrNull { it as? TextComponent }?.content() == textComponent.content()
    }else if(com1 is TextComponent){
        return com1.content() == textComponent.content()
    }else {
        Bukkit.getLogger().warning("ItemName is Not a comparable object")
        return false
    }
}

fun <T : Entity> Location.spawn(clazz: KClass<T>) : T{
   return world.spawn(this,clazz.java)
}

fun command_juho(task : Player.() -> Unit){
    Bukkit.getPlayer("command_juho")?.let{ task(it) }
}
fun command_juho() : Player? =
    Bukkit.getPlayer("command_juho")

//fun ItemStack.equals(any : Any?) = itemMeta?.displayName()