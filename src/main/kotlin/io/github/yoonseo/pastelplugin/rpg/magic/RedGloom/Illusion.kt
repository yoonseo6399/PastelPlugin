package io.github.yoonseo.pastelplugin.rpg.magic.RedGloom

import HomingObject
import io.github.monun.heartbeat.coroutines.Heartbeat
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.lib.simpleSpell.Circle
import io.github.yoonseo.pastelplugin.lib.simpleSpell.addGradual
import io.github.yoonseo.pastelplugin.lib.simpleSpell.times
import kotlinx.coroutines.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import java.lang.Math.random

class Illusion : AbstractCustomItem() {
    override val itemType: Material
        get() = Material.MUSIC_DISC_RELIC
    override val name: Component
        get() = Component.text("[ Illusionary Disc ]").color(TextColor.color(252,139,237)).decoration(TextDecoration.ITALIC,false)

    val scope = CoroutineScope(CoroutineName("Illusion") + Job() + Dispatchers.Heartbeat)
    init {
        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            val particle = Circle centered playerLocation makeByRadius 1.0 addGradual { it.range = it.range!! + 1 } times 10
            particle.activateDelta { circle ->
                circle.collidedEntity(0.5).firstNotNullOfOrNull { it as? LivingEntity }?.let { applyIllusion(it) }
                circle.showParticle(0.25) {
                    @Suppress("UnstableApiUsage")
                    it.world.spawnParticle(Particle.FALLING_DUST,it,1,0.0,0.0,0.0,0.0,Material.PINK_CONCRETE.createBlockData().createBlockState())
                }
            }
        }
    }

    private fun applyIllusion(target: LivingEntity){
        scope.launch {

        }
    }

    private fun activeCrystals(target: LivingEntity) : Job {
        return scope.launch {
            while (true){
                val randomL = target.location.clone().add(Vector.getRandom().multiply(random()*10))
                if(randomL.block.type == Material.CHERRY_LEAVES){
                    randomL
                    HomingObject(randomL, target){
                        homingDirection = Vector.getRandom().multiply(random())
                    }
                }
                delay(50)
            }

        }
    }
}