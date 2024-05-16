package io.github.yoonseo.pastelplugin.rpg.magic.RedGloom

import HomingObject
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.*
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.AdvancedInteractConditions
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.lib.simpleSpell.Circle
import io.github.yoonseo.pastelplugin.lib.simpleSpell.addGradual
import io.github.yoonseo.pastelplugin.lib.simpleSpell.times
import io.github.yoonseo.pastelplugin.skillHelper.LightingStrike
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import io.github.yoonseo.pastelplugin.skillHelper.lineTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import org.bukkit.util.Vector

class Maline : AbstractCustomItem(){
    override val itemType: Material
        get() = Material.ENCHANTED_BOOK
    override val name: TextComponent
        get() = Component.text("[ Maline ]")

    init {
        val team =  Bukkit.getScoreboardManager().mainScoreboard.getTeam("Maline-EFFECT") ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("Maline-EFFECT")
                    .apply { color(NamedTextColor.RED) }

        playerInteractionEvent {
            require(Requires.THIS_ITEM)
            require(Requires.RIGHT_CLICK)
            ScheduleRepeating(expireTick = 4){
                spawnLaserElectroBeam(player,target,team)
            }
        }
    }






    fun spawnLaserElectroBeam(player : Player,target: LivingEntity,team : Team){
        player.location.world.playSound(player.location,org.bukkit.Sound.BLOCK_CONDUIT_DEACTIVATE,10f,1f)


        val direction = player.location.direction
        HomingObject(player.location,target,player){
            homingDirection = direction.multiply(1.2).add(randomDirection(10)).normalize()
            rotationLimit = 10000f
            Delay((targetEntity.location distanceTo shooter.location).toLong()/2){ rotationLimit = 1f }
            maxSpeed = 10f
            cancelWhenTargetIsntExist = true
            collideDetectionRange = 5.0

            everyMovement = {
                    location.world.spawnParticle(ELECTRIC_SPARK,location,1,0.0,0.0,0.0,0.0)

                if(target.location distanceTo player.location < 6) rotationLimit = 1f
            }


            whenCollusion = { target ->

                if(target == null) {
                    kill()
                }else if(target != player){
                    val laser = location.clone().add(homingDirection.clone().multiply(0)) lineTo location.clone().add(homingDirection.clone().multiply(100))

                    laser.showParticle(REDSTONE,0.25,DustOptions(Color.RED,1f))
                    target.forceDamage(5.0)
                    target.addPotionEffect(PotionEffect(PotionEffectType.WITHER,20*4,3,false,true))
                    team.addEntity(target)
                    target.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,20*1,1,false,false))
                    Delay(20) { team.removeEntity(target) }
                    player.location.world.playSound(player.location,org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST,5f,1.1f)

                    val magicCircleParticle = SOUL

                    val effectOb = Circle centered target.location makeByRadius 5.0
                        effectOb.showParticle(0.25){
                            it.world.spawnParticle(magicCircleParticle,it,1,0.0,0.0,0.0,0.0)
                        }
                    val pointA = target.location.clone().add(4.9,0.0,0.0)
                    val pointB = target.location.clone().add(0.0,0.0,4.9)
                    val pointC = target.location.clone().add(-4.9,0.0,0.0)
                    val pointD = target.location.clone().add(0.0,0.0,-4.9)

                    val lines = listOf(
                        pointA lineTo pointB,
                        pointB lineTo pointC,
                        pointC lineTo pointD,
                        pointD lineTo pointA
                    )
                    lines.forEach {
                        it.showParticle(magicCircleParticle,0.25)
                    }
                    val effectOb2 = Circle centered target.location makeByRadius 3.25
                    effectOb2.showParticle(0.25){
                        it.world.spawnParticle(magicCircleParticle,it,1,0.0,0.0,0.0,0.0)
                    }
                    if(target.health == 0.0) LightingStrike(target.location,1.5,10).create(CHERRY_LEAVES, Vector(0.0,1.0,0.0))

                    kill()
                }
            }
        }.launch()
    }


}
