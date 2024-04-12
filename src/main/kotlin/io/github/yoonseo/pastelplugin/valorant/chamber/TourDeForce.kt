package io.github.yoonseo.pastelplugin.valorant.chamber

import RayCast
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.distanceTo
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.AdvancedInteractConditions
import io.github.yoonseo.pastelplugin.itemHandlers.CustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.util.Vector
import kotlin.math.roundToInt

class TourDeForce : AbstractCustomItem() {
    override val name: TextComponent = Component.text("[ Tour de Force ]")
    override val itemType: Material = Material.BLAZE_ROD

    init {
        playerInteractionEvent { e ->
            require(Requires.THIS_ITEM)
            require(Requires.LEFT_CLICK)
            val direction = e.player.location.direction
            val location = e.player.location
            RayCast.runWithCode<LivingEntity>(e.player.location,e.player.location.direction,0.0,100.0,{ it == e.player }){loc ->
                val distance = loc distanceTo e.player.location
                val circleingVec = direction.clone().crossProduct(Vector(1,0,0)).multiply(0.25).rotateAroundAxis(direction,distance)
                val circleingPoint = loc.clone().add(circleingVec)
                val circleingPoint2 = loc.clone().add(circleingVec.multiply(-1))


                loc.world.spawnParticle(Particle.DRAGON_BREATH,circleingPoint,1,0.0,0.0,0.0,0.0)
                loc.world.spawnParticle(Particle.DRAGON_BREATH,circleingPoint2,1,0.0,0.0,0.0,0.0)
                loc.world.spawnParticle(Particle.END_ROD,loc,1,0.0,0.0,0.0,0.0)
//                debug(distance.roundToInt())
//                debug(direction)
//                debug(loc)
//                debug(circleingPoint)
//                debug(circleingVec)

                //loc.world.spawnParticle(Particle.DRAGON_BREATH,loc,1,0.0,0.0,0.0,0.0)
            }?.firstOrNull()?.apply {
                damage(10.0)
                debug(this)
            }
        }
    }

}