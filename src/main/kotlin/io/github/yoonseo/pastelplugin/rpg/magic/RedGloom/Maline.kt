package io.github.yoonseo.pastelplugin.rpg.magic.RedGloom

import HomingObject
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.yoonseo.pastelplugin.*
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.AdvancedInteractConditions
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
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
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team

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
            spawnLaserElectroBeam(player,target,team)
            ScheduleRepeating(expireTick = 4){

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
            maxSpeed = 5f
            cancelWhenTargetIsntExist = true

            everyMovement = {
                location.world.spawnParticle(Particle.REDSTONE,location,1,0.0,0.0,0.0,0.0,DustOptions(Color.fromRGB(192,0,209),1f))
                if(target.location distanceTo player.location < 6) rotationLimit = 1f
            }

            whenCollusion = { target ->

                if(target == null) {
                    kill()
                }else if(target != player){
                    val laser = location.clone().add(homingDirection.clone().multiply(-10)) lineTo location.clone().add(homingDirection.clone().multiply(20))

                    laser.showParticle(Particle.REDSTONE,0.1,DustOptions(Color.RED,1f))
                    target.forceDamage(5.0)
                    target.addPotionEffect(PotionEffect(PotionEffectType.WITHER,20*4,3,false,true))
                    team.addEntity(target)
                    target.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,20*1,1,false,false))
                    Delay(20) { team.removeEntity(target) }
                    player.location.world.playSound(player.location,org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST,5f,1.1f)
                    random(20){
                        LightingStrike(target.location,1.0,5).create(REDSTONE,homingDirection,DustOptions(Color.fromRGB(192,0,209),1f)) {

                        }
                    }

                    kill()
                }
            }
        }.launch()
    }


}
