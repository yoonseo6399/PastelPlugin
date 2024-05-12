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
import org.bukkit.Particle.DustOptions
import org.bukkit.Particle.END_ROD
import org.bukkit.entity.LivingEntity
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
            HeartbeatScope().launch {
                repeat(4){
                    target
                    playerLocation.world.playSound(playerLocation,org.bukkit.Sound.BLOCK_CONDUIT_DEACTIVATE,10f,1f)

                    HomingObject(playerLocation,target,player){
                        homingDirection = direction.multiply(1.5).add(randomDirection(10)).normalize()
                        rotationLimit = 10000f
                        Delay((target.location distanceTo playerLocation).toLong()/3){ rotationLimit = 1f }
                        maxSpeed = 5f
                        cancelWhenTargetIsntExist = true

                        everyMovement = {
                            location.world.spawnParticle(Particle.REDSTONE,location,1,0.0,0.0,0.0,0.0,DustOptions(Color.fromRGB(192,0,209),1f))
                            if(target.location distanceTo playerLocation < 10) rotationLimit = 1f
                        }

                        whenCollusion = {

                            if(it == null) {
                                kill()
                            }else if(it != player){
                                val laser = location.clone().add(homingDirection.clone().multiply(-10)) lineTo location.clone().add(homingDirection.clone().multiply(20))

                                laser.showParticle(Particle.REDSTONE,0.1,DustOptions(Color.RED,1f))
                                it.forceDamage(5.0)
                                it.addPotionEffect(PotionEffect(PotionEffectType.WITHER,20*4,3,false,true))
                                team.addEntity(it)
                                it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,20*1,1,false,false))
                                Delay(20) { team.removeEntity(it) }
                                playerLocation.world.playSound(playerLocation,org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST,5f,1.1f)
                                LightingStrike(target.location,3.0,20).create(END_ROD,homingDirection) {

                                }

                                kill()
                            }



                        }
                    }.launch()
                    delay(100)
                }
            }
        }
    }
    fun spawnLaserElectroBeam(a : AdvancedInteractConditions,team : Team){
        a.apply {

        }
        }

}
