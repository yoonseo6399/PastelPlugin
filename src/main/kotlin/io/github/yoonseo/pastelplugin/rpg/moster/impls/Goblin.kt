package io.github.yoonseo.pastelplugin.rpg.moster.impls

import RayCast
import com.destroystokyo.paper.entity.Pathfinder
import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterStat
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pillager
import org.bukkit.entity.ZombieVillager
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.reflect.KClass

class Goblin : Monster<ZombieVillager>() {
    override var stats: MonsterStat = MonsterStat(
        1,2.0,0.2,15.0,1.0,5.0,0.2
    )
    override val monsterMainClass: KClass<ZombieVillager> = ZombieVillager::class
    override val name: Component = Component.text("고블린")

    override val spawnInitiator: ((ZombieVillager) -> Unit) = {
        if(Math.random() * 100 >= 15){
            it.equipment.setItemInMainHand(ItemStack(Material.WOODEN_SWORD))
        }else {
            it.equipment.setItemInMainHand(ItemStack(Material.WOODEN_AXE))
        }


        it.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,1,false,false))
        it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,PotionEffect.INFINITE_DURATION,1,false,false))
    }

}
class DartGoblin : Monster<Pillager>() {
    override var stats: MonsterStat = MonsterStat(
        1,1.5,0.2,15.0,1.0,5.0,0.2
    )
    override val monsterMainClass: KClass<Pillager> = Pillager::class
    override val name: Component = Component.text("다트 고블린")

    override val spawnInitiator: ((Pillager) -> Unit) = {
        it.equipment.setItemInMainHand(ItemStack(Material.CROSSBOW))
        it.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,1,false,false))
        it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,PotionEffect.INFINITE_DURATION,1,false,false))
    }
    override val whenAttacking: ((EntityDamageByEntityEvent) -> Unit) = {
        if(it.entity is LivingEntity){
            val e = it.entity as LivingEntity
            e.addPotionEffect(PotionEffect(PotionEffectType.POISON,3*20,2))
        }
    }
}