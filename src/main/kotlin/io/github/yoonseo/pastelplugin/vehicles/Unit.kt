package io.github.yoonseo.pastelplugin.vehicles

import org.bukkit.Location
import org.bukkit.entity.BlockDisplay


import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class Unit(
    open var location : Location,
    //TODO Display container
    open val parts : ArrayList<UnitPart> = ArrayList()
) : UnitPart(Vector(0,0,0),"Body") {
    var rotation : Quaternionf = Quaternionf(0.0,0.0,0.0,0.0)

    fun loadDisplay() {
        //load displays
    }

    // Core
    // Sub
    // Rotational
    //
}
abstract class UnitPart(var relativePos: Vector,val name : String){
    lateinit var mainObject : BlockDisplay
    var isDeparted = false
    abstract fun load()
    abstract fun update(data : PartUpdate)
}
data class PartUpdate(
    val centerBodyLocation: Location,
    val relativePos: Vector? = null,
    val isDeparted : Boolean,
    val tilting : Quaternionf,
    val updateRotationQuaternionf: Quaternionf
){

    val localXAxis: Vector = tilting.transform(Vector3f(1.0f, 0.0f, 0.0f)).toBukkit()
    val localYAxis: Vector = tilting.transform(Vector3f(0.0f, 1.0f, 0.0f)).toBukkit()
    val localZAxis: Vector = tilting.transform(Vector3f(0.0f, 0.0f, 1.0f)).toBukkit()
}



fun Vector3f.toBukkit() = Vector(x,y,z)
fun Vector.toJoml3f() = Vector3f(x.toFloat(),y.toFloat(),z.toFloat())