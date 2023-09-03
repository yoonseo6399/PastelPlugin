package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.CancelTask
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.seeVectorTo
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Wither
import org.bukkit.entity.WitherSkull

class WitherKing : Boss("WitherKing") {
    companion object{

    }
    override fun spawn(loc : Location): WitherKing{
        return WitherKing().apply {
            wither = loc.world.spawn(loc,Wither::class.java){
                it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 1000.0
                it.health = 1000.0

                ScheduleRepeating {id ->
                    AI(wither, wither.target) // 타겟이 있다면 AI 실행
                    if(wither.isDead) CancelTask(id) // 죽으면 AI 취소
                }
            }
        }
    }

    lateinit var wither : Wither
    val location : Location
        get() = wither.location

    val AI : Wither.(LivingEntity?) -> Unit = {target ->
        if(target != null){
            location.world.spawn(location,WitherSkull::class.java){
                it.direction = it.location seeVectorTo target.location
            }
        }

    }
}