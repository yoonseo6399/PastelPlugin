package io.github.yoonseo.pastelplugin.rpg.magic.ice

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.Delay
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.display.test
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import io.github.yoonseo.pastelplugin.skillHelper.lineTo
import io.github.yoonseo.pastelplugin.spawn
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.AxisAngle4f
import org.joml.Vector3f

class Friche : AbstractCustomItem(){
    override val name: TextComponent = Component.text("[ 프라이체 ]")
    override val itemType: Material = Material.BLAZE_ROD
    val team by lazy {
        try {
            Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam(this::class.simpleName!!).also { it.color(NamedTextColor.BLUE) }
        } catch (e: Exception) {
            Bukkit.getScoreboardManager().mainScoreboard.getTeam(this::class.simpleName!!)!!
        }
    }

    init {
        playerInteractionEvent { e ->
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)

            val target = Selector(30).selectLivingEntity(player.eyeLocation){ it != player }?.firstOrNull() ?: return@playerInteractionEvent

            val chainAmount = 11
            val randomRange = -100..100
            val chainLoc = ArrayList<Location>(chainAmount)
            repeat(chainAmount){//체인 위치 추가
                ///target.location.clone().add(Vector(randomRange.random()/10,(randomRange.random()/20)+5,randomRange.random()/10)).also { debug(it) }
                val dir = target.location.direction.clone().setY(0.0).multiply(10)
                val loc = target.location.clone().add(dir.rotateAroundY(it.toDouble()*20)).add(0.0,4.0,0.0)
                chainLoc.add(loc)
            }
            chainLoc.forEach {
                it.add(Vector(0.5,0.5,0.5)).spawn(BlockDisplay::class).apply {
                    block = Material.ICE.createBlockData()
                    scoreboardTags.add("SKILLED_FORM")
                    Delay(10*20L){
                        this.remove()
                    }
                }
//                it.add(0.0,-0.4,0.0).spawn(BlockDisplay::class).apply {
//                    block = Material.ICE.createBlockData()
//                    transformation = Transformation(Vector3f(),AxisAngle4f(),Vector3f(0.4F, 0.4F, 0.4F), AxisAngle4f())
//                    scoreboardTags.add("SKILLED_FORM")
//                    Delay(10*20L){
//                        this.remove()
//                    }
//                }
            }
            target.location.world.playSound(target.location,Sound.ITEM_TRIDENT_THUNDER,1f,0.4f)


            team.addEntities(target)

            val loc = target.location

            ScheduleRepeating(expireTick = 10*20) {


                chainLoc.forEach {

                    (it.clone() lineTo target.location.add(0.0,1.0,0.0).clone()).showParticle(Particle.FALLING_DUST,0.2,Material.ICE.createBlockData())
                    target.addPotionEffect(PotionEffect(PotionEffectType.SLOW,20,10,false,false))
                    target.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,20,10,false,false))
                    target.teleport(loc)
                }
            }
        }
    }
}