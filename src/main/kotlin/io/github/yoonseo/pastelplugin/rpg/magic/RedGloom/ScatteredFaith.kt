package io.github.yoonseo.pastelplugin.rpg.magic.RedGloom

import HomingObject
import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.lib.simpleSpell.Circle
import io.github.yoonseo.pastelplugin.lib.simpleSpell.addGradual
import io.github.yoonseo.pastelplugin.lib.simpleSpell.times
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Particle.REDSTONE
import org.bukkit.util.Vector

class ScatteredFaith : AbstractCustomItem(){
    override val itemType: Material = Material.BOOK
    override val name: TextComponent = Component.text("[ BloodAbsorb ]")


    init {
        playerInteractionEvent { e ->
            require(Requires.THIS_ITEM)
            val target = Selector(100).selectLivingEntity(playerLocation) { it != player }?.firstOrNull() ?: return@playerInteractionEvent

            if(e.action.isRightClick){
                target.damage(1.5)
                HomingObject(target.location.clone().add(0.0,2.0,0.0), player){
                    rotationLimit = 2f
                    whenCollusion = {
                        if(it != null && it == player)
                        kill()
                    }
                    everyMovement = {
                        location.world.spawnParticle(Particle.REDSTONE,location,1,0.0,0.0,0.0,0.0,DustOptions(Color.RED,1f))
                        location.world.spawnParticle(Particle.REDSTONE,location,1,0.0,0.0,0.0,0.0,DustOptions(Color.BLACK,0.5f))
                    }
                    val ra = -5..5
                    homingDirection = Vector(ra.random(),ra.random(),ra.random()).normalize()
                }.launch(PastelPlugin.plugin)
            }

            if(e.action.isLeftClick){
                val eff = Circle centered target.location makeByRadius 1.0 addGradual { it.range = it.range!!+1 } times 20
                debug("1")
                eff.activateDelta {
                    it.showParticle(0.5){
                        it.world.spawnParticle(REDSTONE,it,1,0.0,0.0,0.0,0.0,DustOptions(Color.RED,1f))
                    }
                }
            }
        }
    }
}