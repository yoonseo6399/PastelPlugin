package io.github.yoonseo.pastelplugin.rpg.magic.earth

import com.sun.source.doctree.BlockTagTree
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.skillHelper.Forward
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import io.github.yoonseo.pastelplugin.skillHelper.withWide
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.FallingBlock
import org.bukkit.util.Vector

class RollingThunder : AbstractCustomItem(){
    override val itemType: Material = Material.BLAZE_ROD
    override val name: TextComponent = Component.text("[ 지진강타 ]")

    init {
        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            val oppositeV = playerLocation.direction.clone().rotateAroundY(90.0)


            Forward.run(playerLocation,playerLocation.direction,30.0,1.0){ loc ->
                withWide(loc,playerLocation.direction,15){ it1 ->
                    val block = Selector(10.0).selectBlock(it1, Vector(0,-1,0)) { it.isCollidable && it.isSolid }
                    if(block != null){
                        block.location
                        block.location.world.spawn(block.location.clone().add(Vector(0.5,0.5,0.5)), FallingBlock::class.java).apply {
                            blockState = block.state
                            velocity = Vector(0.0,0.4,0.0)
                            cancelDrop = true
                            scoreboardTags.add("SKILLED_FORM")
                        }
                    }
                    loc.world.playSound(loc,Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,1f,0.5f)
                }

            }
        }
    }
}