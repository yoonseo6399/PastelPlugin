package io.github.yoonseo.pastelplugin.boss

import org.bukkit.entity.LivingEntity
import java.util.Collections
import java.util.function.Predicate

class SkillSet(vararg val skills : Pair<Skill,String>) {
    val skillList = hashMapOf(*skills)


}
class Skill<E : LivingEntity>(val cooldown: Int,val skill: (E) -> Unit,val condition: (E) -> Boolean){
    lateinit var en : E
    val cool = CooldownWithTask(cooldown){ skill(en) }
    fun checkConditionAndRun(entity: E): Boolean{
        en = entity
        return if(condition(entity)){
            cool.invoke()
        }else false
    }
}