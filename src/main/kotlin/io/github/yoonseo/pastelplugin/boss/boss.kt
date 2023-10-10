package io.github.yoonseo.pastelplugin.boss

import io.github.monun.kommand.KommandContext
import io.github.monun.kommand.kommand
import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.overworld
import io.github.yoonseo.pastelplugin.spawn
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.EnumSet
import kotlin.reflect.KClass

class bossCommand {
    init{
        PastelPlugin.plugin.kommand {
            register("boss"){
                then("spawn"){
                    then("bossName" to string()){
                        executes {
                            if(!isPlayer) {
                                sender.sendMessage("player only command")
                                return@executes
                            }
                            Boss.spawn(it["bossName"],player.location)
                        }
                    }
                }
            }
        }
    }
}

abstract class Boss(bossName : String){
    companion object{
        val list = HashMap<String,Boss>()
        fun spawn(bossName: String,loc: Location): Boss? {
            return list[bossName]?.spawn(loc)
        }
    }
    lateinit var mainEntity : LivingEntity

    init{
        list[bossName] = this
    }

    abstract fun spawn(loc : Location) : Boss

}


open class CustomEntity<E : LivingEntity>(private val clazz: KClass<E>){
    var isSpawned = false
    var hasCustomAI = false
    var customAI : CustomAI<*>? = null


    private var entityInit : (E.() -> Unit)? = null
    private lateinit var entity : E

    operator fun invoke(): E = if (isSpawned) entity else spawn(overworld.spawnLocation)
    companion object{
        private val entityList = HashMap<KClass<*>,CustomEntity<*>>()

        operator fun <E : CustomEntity<*>> invoke(clazz : KClass<E>): E = clazz.constructors.first().call()
    }

    init{
        entityList[this::class] = this
    }

    fun spawn(location: Location): E{
        entity = location.spawn(clazz)
        entityInit?.invoke(entity)
        isSpawned = true

        return entity
    }

    fun entityInit(init : (E.() -> Unit)) { entityInit = init }




}
