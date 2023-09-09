package io.github.yoonseo.pastelplugin.ThorHammer

import HomingObject
import io.github.yoonseo.pastelplugin.*
import io.github.yoonseo.pastelplugin.itemHandlers.AbstractCustomItem
import io.github.yoonseo.pastelplugin.itemHandlers.Requires
import io.github.yoonseo.pastelplugin.itemHandlers.isKeepClicking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import rayCast

class ThorHammer : AbstractCustomItem() {
    override val name: TextComponent = Component.text("[ Thor Hammer ]").style(Style.style(NamedTextColor.GOLD))
    override val itemType: Material = Material.IRON_AXE
    private var hammerAvailable = true
    private var hammerObject : HomingObject? = null
    private var isReturning = false
    private var lastAttackEntity : LivingEntity? = null
    //hammer 기본설정
    private val initHammer : HomingObject.() -> Unit = {
        acceleration = 0.5f
        startingSpeed = 2f
        maxSpeed = 20f

        whenCollusion = { livingEntity: LivingEntity? ->
            if(livingEntity == shooter){
                if(isReturning){
                    hammerAvailable = true
                    isReturning = false
                    kill()
                }
            }else if(livingEntity == null) {// 땅에 부딛힘?
                //location.world.createExplosion(location,4f)
                hammerObject?.homingDirection?.add(Vector(1,1,0))
                lastAttackEntity = null
            }else if(lastAttackEntity != livingEntity){ // 무한번개 막기
                lastAttackEntity = livingEntity
                livingEntity.damage(30.0,shooter)
                livingEntity.velocity = location.direction.multiply(2)
                location.world.spawn(location,LightningStrike::class.java)
                location.world.spawnParticle(Particle.END_ROD,location,40)

                //연속타격 가능하게 수정
                Delay(3){
                    lastAttackEntity = null
                }
            }
        }
        everyMovement = {
            location.world.spawnParticle(Particle.SOUL_FIRE_FLAME,location,1,0.0,0.0,0.0,0.0)
            targetEntity.sendActionBar("you targeted".toComponent())
        }
    }

    private var taskId = -1
    init{
        playerInteractionEvent {
            //e.player.sendMessage("init - playerInter")
            require(Requires.THIS_ITEM)
            //e.player.sendMessage("init - R1")
            require(Requires.RIGHT_CLICK)
            //e.player.sendMessage("init - R2")

            if(taskId == -1) ScheduleRepeating(cycle = 1){
                //player.sendActionBar(Component.text("hamm : $hammerAvailable , isReturning : $isReturning , hommingTo: ${hammerObject?.targetEntity}"))
                //player.sendMessage(player.isKeepClicking.toString())
                if(!player.isKeepClicking){
                    cancelHoming(player)
                    return@ScheduleRepeating
                }
                val targets = player.rayCast(1.0,50.0) ?: return@ScheduleRepeating

                Bukkit.getLogger().info(player.displayName)
                if(hammerAvailable){
                    lastAttackEntity = null
                    HomingObject(playerLocation, targets.first(),player,initHammer).apply{
                        targetEntity = targets.first()
                        launch(PastelPlugin.plugin)
                        hammerObject = this
                    }
                    hammerAvailable = false
                }else{
                    hammerObject?.targetEntity = targets.first()
                }
            }.also {
                taskId = it
            }
        }

        playerItemChangeToOtherItemEvent {
            cancelHoming(it.player)
        }
    }

    private fun cancelHoming(p : Player){
        p.sendActionBar(Component.text("CancelHoming and Returning").style(Style.style(NamedTextColor.RED)))
        hammerObject?.targetEntity = p
        isReturning = true
        CancelTask(taskId)
        taskId = -1
    }
}