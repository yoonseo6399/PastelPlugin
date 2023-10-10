package io.github.yoonseo.pastelplugin


import getDistance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.reflect.KClass


infix fun ItemStack.isNamed(name : String) =
    (this.itemMeta?.displayName() as? TextComponent)?.content() == name
infix fun ItemStack.isntNamed(name : String) =
    (this.itemMeta?.displayName() as? TextComponent)?.content() != name




fun ScheduleRepeating(cycle: Long = 1,delay: Long = 0,expireTick: Long = -1,taskName: String = "",task: (Int) -> Unit): Int{
    val plugin = PastelPlugin.plugin
    if(taskName != "" && PastelPlugin.taskList.contains(taskName)) return -1

    var id = -1
    id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,{
        task(id)
    },delay,cycle)
    if(expireTick > 0){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,{ Bukkit.getScheduler().cancelTask(id);PastelPlugin.taskList.remove(taskName) },expireTick)
    }
    if(taskName != "") PastelPlugin.taskList[taskName] = id
    return id
}

fun RepeatUntil(cycle: Long = 1,delay: Long = 0,expireTick: Long = -1,taskName: String = "",until : () -> Boolean,task: (Int) -> Unit): Int =
    ScheduleRepeating(cycle,delay,expireTick,taskName) {task(it); if(until()) CancelTask(it) }


fun CancelTask(id: Int) {
    Bukkit.getScheduler().cancelTask(id)
}
fun CancelTask(taskName: String){
    PastelPlugin.taskList[taskName]?.let { Bukkit.getScheduler().cancelTask(it);PastelPlugin.taskList.remove(taskName) }
}

fun Delay(ticks: Long,task: Runnable): Int{
    return Bukkit.getScheduler().scheduleSyncDelayedTask(PastelPlugin.plugin,task,ticks)
}

infix fun Location.distanceTo(loc : Location) = getDistance(this,loc)

fun Mob.getDistanceFromTarget() : Double? = target?.let { it.location distanceTo location }

fun Mob.isTargetInRange(range: Double) = getDistanceFromTarget()?.let { it <= range } == true


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

fun Component.toText() =
    (this as TextComponent).content()

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

//fun ItemStack.equals(any : Any?) = itemMeta?.displayName()