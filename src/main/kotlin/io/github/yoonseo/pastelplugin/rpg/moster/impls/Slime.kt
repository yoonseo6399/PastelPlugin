package io.github.yoonseo.pastelplugin.rpg.moster.impls

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterStat
import io.github.yoonseo.pastelplugin.toText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Slime
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

class Slime : Monster<Slime>() {
    override var stats: MonsterStat = MonsterStat(
        7,
        5.0,
        1.2,
        10.0,
        1.5,
        5.0,
        2.0
    )
    override val monsterMainClass = Slime::class
    override val name: Component = Component.text("슬라임").color(NamedTextColor.GREEN)

    override val spawnInitiator: ((Slime) -> Unit) ={
        it.size = 4
    }

    override val whenAttacking: (EntityDamageByEntityEvent) -> Unit = {e ->
        if(e.entity is LivingEntity){
            (e.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.SLOW,4*20,1))
            (e.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.POISON,3*20,1))
        }
    }
    override val whenAttacked: ((EntityDamageByEntityEvent) -> Unit) = {
        it.entity.location.world.spawnParticle(Particle.END_ROD,it.entity.location,1)

        val damage = it.damage / (1 + (0.03 * stats.getLeveledDefense()))
        //debug((it.entity as Slime).health)
        //debug(damage)
        if((it.entity as Slime).health - damage <= 0){
            (it.entity as Slime).remove()
            it.isCancelled = true
        }
    }
    init {
        debug(getNickName().toText())
    }
}