package io.github.yoonseo.pastelplugin.rpg.moster.impls

import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import io.github.yoonseo.pastelplugin.rpg.moster.MonsterStat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Wolf
import kotlin.reflect.KClass

class Wolf : Monster<org.bukkit.entity.Wolf>(){
    override var stats: MonsterStat = MonsterStat(
        1,//레벨
        1.0,//힘
        1.5,//래밸당 힘 증가량
        15.0,//체력
        2.0,//레벨당 체력 증가량
        2.0,// 방어력
        2.0

    )
    override val monsterMainClass: KClass<Wolf> = org.bukkit.entity.Wolf::class
    override val name: Component = Component.text("굼주린 늑대").color(NamedTextColor.BLUE)
}