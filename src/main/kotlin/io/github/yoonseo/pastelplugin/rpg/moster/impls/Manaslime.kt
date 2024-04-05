package io.github.yoonseo.pastelplugin.rpg.moster.impls

import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterStat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Slime
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

class ManaSlime : Monster<org.bukkit.entity.Slime>() {
    override var stats: MonsterStat = MonsterStat(
        5,//레벨
        2.0,//힘
        3.0,//래밸당 힘 증가량
        15.0,//체력
        2.0,//레벨당 체력 증가량
        1.0,// 방어력
        1.0

    )
    override val monsterMainClass: KClass<Slime> = org.bukkit.entity.Slime::class
    override val name: Component = Component.text("마나슬라임").color(NamedTextColor.BLUE)

    override val spawnInitiator: ((Slime) -> Unit) = {
        it.size = 2
    }

    //override val whenAttacking: ((EntityDamageByEntityEvent) -> Unit) = {
        //it.entity // 공격받은새끼
        //if(it.entity is LivingEntity) {
            //val e = it.entity as LivingEntity


            //e.addPotionEffect(PotionEffect(PotionEffectType.POISON,5*20,1))
    //    }
    //}

}