package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.CancelTask
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import org.bukkit.entity.Mob
import kotlin.reflect.KClass

class CustomAI<E : Mob>(val clazz: KClass<E>,val repeatedAI : E.() -> Unit) {
    var id = -1
    fun stop(){
        CancelTask(id)
    }
    fun run(entity : E){
        id = ScheduleRepeating {
            repeatedAI(entity)
        }
    }
}