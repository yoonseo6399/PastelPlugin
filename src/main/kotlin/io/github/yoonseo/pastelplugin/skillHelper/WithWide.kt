package io.github.yoonseo.pastelplugin.skillHelper

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.util.Vector

fun withWide(startLocation: Location, startDir: Vector, width: Int, block : (Location) -> Unit){

    val op = startDir.clone().rotateAroundY(90.0)
    val a = startLocation.clone().add(op.multiply(width/2))
    op.multiply(-1).normalize()
    repeat(width){
        block(a.add(op))
    }
}