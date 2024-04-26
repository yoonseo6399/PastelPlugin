package io.github.yoonseo.pastelplugin.rpg.magic.fire

import io.github.yoonseo.pastelplugin.Delay
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.skillHelper.Forward
import io.github.yoonseo.pastelplugin.spawn
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity

class Fireball: AbstractCustomItem() {
    override val itemType: Material = Material.FIRE_CHARGE
    override val name: TextComponent
        get() = Component.text("[ 파이어볼 ]")

    init {
        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.LEFT_CLICK)
            val fireball = player.eyeLocation.spawn(Fireball::class)

            fireball.velocity = playerLocation.direction.multiply(0.5)
        }
    }
}