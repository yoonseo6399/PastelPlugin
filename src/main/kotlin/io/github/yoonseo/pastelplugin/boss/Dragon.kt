package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.*
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.Mob

class Dragon : CustomEntity<EnderDragon>(EnderDragon::class){
    val skills = SkillSet(
        Skill<Mob>(100,{
            it.target!!.location.spawn(LightningStrike::class)
        }) { it.isTargetInRange(50.0) } to "LightingStrike",

        Skill<Mob>(200,{
            it.target!!//thinking
        }) { it.isTargetInRange(50.0) } to "Levitation",
    )



    init {





        entityInit {
            getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 1000.0
            health = 1000.0
        }
        customAI = CustomAI(EnderDragon::class){
            if(target != null){// 타겟이 존재하면

            }
        }
    }
}
