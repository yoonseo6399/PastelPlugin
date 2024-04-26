package io.github.yoonseo.pastelplugin.valorant.Jett

import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.toComponent
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material

class  Dash : AbstractCustomItem() {
    override val name: TextComponent = "데쉬".toComponent()
    override val itemType: Material = Material.MAGENTA_GLAZED_TERRACOTTA

    init {
        whenSelected {
            it.player.velocity = it.player.location.direction.multiply(2)
            it.isCancelled = true

        }
    }
}