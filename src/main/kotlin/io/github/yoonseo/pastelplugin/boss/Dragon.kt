package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.*
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob

class Dragon : CustomEntity<EnderDragon>(EnderDragon::class){
    val skills = SkillSet<EnderDragon>(
        "LightingStrike" to Skill(100,{
            debug("LightingStrike!!!!!!!!!!!!!!")
            it.pathfinder.moveTo(it.customTarget!!)
            it.customTarget!!.location.spawn(LightningStrike::class)

        }) { it.isTargetInRange(10.0) },

        "Levitation" to Skill(200,{

        }) { it.isTargetInRange(50.0) },
    )



    init {
        OnSpawn {
            entityInit {
                getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 1000.0
                health = 1000.0
            }
            customAI = CustomAI(EnderDragon::class){
                if(target != null){// 타겟이 존재하면
                    debug("target lock")
                }
            }
            skills.autoActive(super.invoke())
            OnDeath {
                skills.autoActiveCancel()
            }
        }
    }
}

