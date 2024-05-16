package io.github.yoonseo.pastelplugin.rpg.magic.wind

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.skillHelper.Forward
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector

class Locus : AbstractCustomItem() {
    override val itemType: Material
        get() = Material.FIREWORK_STAR
    override val name: TextComponent
        get() = Component.text("[ Locus ]")

    init {
        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            cooldown(100)
            playerLocation.world.playSound(playerLocation,Sound.BLOCK_CALCITE_BREAK,1f,1f)

            Forward.run(player.eyeLocation,playerLocation.direction,40.0, tickRepeating = 4){
                val target = it.world.getNearbyEntities(it,1.0,1.0,1.0){ it != player }.firstOrNull()
                it.world.spawnParticle(Particle.END_ROD,it,1,0.0,0.0,0.0,0.0)


                if(target != null){
                    //debug(target)
                    target.velocity = target.velocity.add(direction.multiply(1))//.add(Vector(0.0,1.0,0.0)))
                    target.location.world.spawnParticle(Particle.GUST_EMITTER,target.location,1)
                    playerLocation.world.playSound(target.location,Sound.ENTITY_GENERIC_EXPLODE,1f,1f)
                    cancel()
                }
                if(it.world.getBlockAt(it).isCollidable){
                    it.world.createExplosion(it,3f,false)
                }
            }
        }
    }
}