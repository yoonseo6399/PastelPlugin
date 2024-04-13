package io.github.yoonseo.pastelplugin.rpg.magic.earth

import RayCast
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.PointedDripstone
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class Valagart : AbstractCustomItem(){
    override val name: TextComponent = Component.text("[ 발르가트 ]")
    override val itemType: Material = Material.BLAZE_ROD

    init {
        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            val player = it.player
            val location = player.location.clone()
            val direction = location.direction.clone()
            val nidir = direction.clone().crossProduct(Vector(0.0,1.0,0.0)).multiply(1)
            HeartbeatScope().launch {
                repeat(15){
                    location.add(direction.multiply(1))

                    RayCast.runBlock<Block>(location,Vector(0,-1,0),3.0)?.let {
                        val sLoc = it.location.clone().add(Vector(0.5,0.0,0.5)).add(nidir)
                        nidir.multiply(-1)
                        repeat(3){
                            sLoc.world.spawn(sLoc,FallingBlock::class.java).apply {
                                blockState = Material.POINTED_DRIPSTONE.createBlockData().createBlockState()
                                velocity = Vector(0.0,0.3,0.0)
                            }
                            val target = sLoc.world.getNearbyEntities(sLoc,0.0,1.0,0.0) { it != player && it is LivingEntity }.firstOrNull() as? LivingEntity

                            target?.damage(10.0,player)

                            sLoc.add(nidir)
                        }

                    }


                    delay(25)//1tick
                }
            }
        }
    }

}