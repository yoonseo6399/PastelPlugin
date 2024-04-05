package io.github.yoonseo.pastelplugin.rpg.moster

import io.github.yoonseo.pastelplugin.PastelPlugin
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.plus
import io.github.yoonseo.pastelplugin.spawn
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Mob
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberFunctions

abstract class Monster<E : Mob> {
    companion object{
        fun <E : Mob,T : io.github.yoonseo.pastelplugin.rpg.moster.Monster<E>> spawn(monster: KClass<T>,loc : Location) : T{
            val instance = monster.constructors.first().call()

            monster.memberFunctions.map { it.name }.forEach { debug(it) }
            debug("end")
            monster.memberFunctions.find { it.name.contains("spawn") }!!.call(instance,loc)
            return instance
        }
    }

    fun spawn(loc : Location){
        mob = loc.spawn(monsterMainClass).apply {
            spawnInitiator?.let { it(this) }

            try {
                getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 0.0
                getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.addModifier(AttributeModifier("defualt",stats.getLeveledHealth(),AttributeModifier.Operation.ADD_NUMBER))
                stats.getLeveledHealth().also { health = it }
            }catch (e : IllegalArgumentException){
                debug("there is problem ${e.message}")
            }

            customName(getNickName())
            isCustomNameVisible = true
        }
        //Defealt handler
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler(priority = EventPriority.HIGHEST)
            fun whenAttacked(e: EntityDamageByEntityEvent){
                if(e.entity == mob) {
                    e.damage /= (1 + (0.03 * stats.getLeveledDefense()))
                    debug(e.damage)
                }
                if(e.damager == mob) {
                    e.damage += e.damage*(0.03 * stats.getLeveledStrength())
                    debug(e.damage)
                    debug(stats.getLeveledStrength())
                }
            }
        },PastelPlugin.plugin)

        if(whenAttacking != null){
            Bukkit.getPluginManager().registerEvents(object : Listener {
                @EventHandler(priority = EventPriority.LOW)
                fun whenAttacking(e: EntityDamageByEntityEvent){
                    if(e.damager == mob) this@Monster.whenAttacking!!(e)
                }
            },PastelPlugin.plugin)
        }
        if(whenAttacked != null){
            Bukkit.getPluginManager().registerEvents(object : Listener {
                @EventHandler(priority = EventPriority.LOW)
                fun whenAttacked(e: EntityDamageByEntityEvent){
                    if(e.entity == mob) this@Monster.whenAttacked!!(e)
                }
            },PastelPlugin.plugin)
        }
        if(whenDeath != null){
            Bukkit.getPluginManager().registerEvents(object : Listener {
                @EventHandler(priority = EventPriority.LOW)
                fun whenDeath(e: EntityDeathEvent){
                    if(e.entity == mob) this@Monster.whenDeath!!(e)
                }
            },PastelPlugin.plugin)
        }

        //lisener
    }
    var mob : E? = null
    abstract var stats : MonsterStat
    abstract val monsterMainClass : KClass<E>
    open val whenAttacking : ((EntityDamageByEntityEvent) -> Unit)? = null
    open val whenAttacked : ((EntityDamageByEntityEvent) -> Unit)? = null
    open val whenDeath : ((EntityDeathEvent) -> Unit)? = null
    open val spawnInitiator : ((E) -> Unit)? = null
    var prefix : Component? = null
    abstract val name : Component


    fun getNickName() : Component {
        if(prefix != null){
            return Component.text("[ ").color(NamedTextColor.WHITE) + prefix!! + Component.text("lv.${stats.level} ") + name + Component.text(" ]").color(NamedTextColor.WHITE)
        }else{
            return Component.text("[ ").color(NamedTextColor.WHITE) + Component.text("lv.${stats.level} ") + name + Component.text(" ]").color(NamedTextColor.WHITE)
        }
    }
}

data class MonsterStat(
    val level: Int,
    val strength : Double,
    val strengthMultiplier: Double,
    val health : Double,
    val healthMultiplier: Double,
    val defense : Double,
    val defenseMultiplier: Double,
){
    fun getLeveledHealth() : Double{
        return level*healthMultiplier+health
    }
    fun getLeveledStrength() : Double{
        return level*strengthMultiplier+strength
    }
    fun getLeveledDefense() : Double{
        return level*defenseMultiplier+defense
    }


}