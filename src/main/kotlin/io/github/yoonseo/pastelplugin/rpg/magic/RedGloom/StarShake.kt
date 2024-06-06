package io.github.yoonseo.pastelplugin.rpg.magic.RedGloom

import com.destroystokyo.paper.ParticleBuilder
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.lib.simpleSpell.Circle
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Particle

class StarShake : AbstractCustomItem(){
    override val itemType: Material
        get() = Material.NETHER_STAR
    override val name: Component
        get() = Component.text("[ StarShake ]")

    init {
        playerInteractionEvent {
            require(Requires.RIGHT_CLICK)
            require(Requires.THIS_ITEM)

            val energeC = Circle centered playerLocation rotatedByAxis playerLocation.direction makeByRadius 5.0


            energeC.showParticle(1.0){ it.world.spawnParticle(Particle.END_ROD,it,0,0.0,0.0,0.0,Particle)}

        }
    }
}