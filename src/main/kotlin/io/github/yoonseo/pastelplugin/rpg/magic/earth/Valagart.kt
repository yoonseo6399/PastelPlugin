package io.github.yoonseo.pastelplugin.rpg.magic.earth

import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.lib.AdjustableOf
import io.github.yoonseo.pastelplugin.plugin
import io.github.yoonseo.pastelplugin.skillHelper.Forward
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.util.Vector

class Valagart : AbstractCustomItem(){
    override val name: TextComponent = Component.text("[ 발르가트 ]")
    override val itemType: Material = Material.BLAZE_ROD


    var speed by "speed" AdjustableOf 1


    init {
        val listener = object : Listener {
            @EventHandler
            fun cancelFallingToBlock(e : EntityChangeBlockEvent){
                if(e.entity is FallingBlock && e.entity.scoreboardTags.contains("SKILLED_FORM")) {
                    e.isCancelled = true
                    e.entity.remove()
                }
            }
        }
        Bukkit.getPluginManager().registerEvents(listener, plugin)


        playerInteractionEvent { e ->
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            val player = e.player
            val location = player.eyeLocation.clone()
            val direction = location.direction.clone()
            val nidir = direction.clone().crossProduct(Vector(0.0,1.0,0.0)).multiply(1)

            Forward.run(location,direction,15.0, oneBlockDelay = 25L, tickRepeating = speed){loc ->
                nidir.multiply(-1)
                repeat(3){i -> //너비 3

                    val sLoc = Selector(3).underBlock(loc){ it.isCollidable }?.location?.clone()?.add(Vector(0.5,0.0,0.5))?.add(nidir.clone().multiply(-1)) ?: return@repeat
                    sLoc.add(nidir.clone().multiply(i))
                    sLoc.world.spawn(sLoc,FallingBlock::class.java).apply {
                        blockState = Material.POINTED_DRIPSTONE.createBlockData().createBlockState()
                        velocity = Vector(0.0,0.3,0.0)
                        cancelDrop = true
                        scoreboardTags.add("SKILLED_FORM")
                    }

                    val target = sLoc.world.getNearbyEntities(sLoc,0.3,2.0,0.3) { it != player && it is LivingEntity }.mapNotNull { it as? LivingEntity }
                    target.forEach {
                        it.damage(5.0,player)
                        it.velocity.add(Vector(0.0,0.4,0.0))
                    }



                    sLoc.add(nidir)
                }
            }
        }
    }

}