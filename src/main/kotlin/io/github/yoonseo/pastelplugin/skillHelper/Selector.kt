@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.yoonseo.pastelplugin.skillHelper

import RayCast
import com.google.common.base.Predicate
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class Selector(val range : Number) {
    fun selectLivingEntity(startLocation: Location,direction: Vector,predicate: (LivingEntity) -> Boolean = {true}) : List<LivingEntity>?{
        return selectEntity<LivingEntity>(startLocation,direction,predicate)
    }
    fun selectLivingEntity(startLocation: Location,predicate: (LivingEntity) -> Boolean = {true}) : List<LivingEntity>? =
        selectLivingEntity(startLocation,startLocation.direction,predicate)
    /**pick a block except air*/
    fun selectBlock(startLocation: Location,direction: Vector,predicate: (Block) -> Boolean = {true}) : Block?{
        return RayCast.runBlock<Block>(startLocation,direction,range.toDouble(),predicate)
    }
    fun selectBlock(startLocation: Location,predicate: (Block) -> Boolean = {true}) =
        selectBlock(startLocation,startLocation.direction,predicate)

    inline fun <reified T : Entity> selectEntity(startLocation: Location, direction: Vector, crossinline predicate: (T) -> Boolean = {true}) : List<T>?{
        return RayCast.run<T>(startLocation,direction,0.1,range.toDouble()){
            !predicate(it as T)
        }
    }
    inline fun <reified T : Entity> selectEntity(startLocation: Location, crossinline predicate: (T) -> Boolean = {true}) : List<T>? =
        selectEntity(startLocation,startLocation.direction,predicate)

    inline fun <reified T : Entity>byRange(location: Location, crossinline predicate: (T) -> Boolean) =
        location.world.getNearbyEntities(location,range.toDouble(),range.toDouble(),range.toDouble()) { it is T && predicate(it) }
            .map { it as T }.ifEmpty { null }

    fun underBlock(startLocation: Location,predicate: (Block) -> Boolean = {true}) = selectBlock(startLocation,Vector(0.0,-1.0,0.0),predicate)
}

