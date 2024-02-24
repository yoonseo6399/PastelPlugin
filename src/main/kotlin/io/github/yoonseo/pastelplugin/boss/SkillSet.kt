package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.CancelTask
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.debug
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.LivingEntity
import java.util.Collections
import java.util.function.Predicate

@Suppress("MemberVisibilityCanBePrivate")
class SkillSet<E : LivingEntity>(vararg skills : Pair<String,Skill<E>>) {
    val skillList = hashMapOf(*skills)
    private var autoActiveID = -1

    fun autoActive(entity : E){
        autoActiveID = ScheduleRepeating {
            skillList.values.forEach { it.checkConditionAndRun(entity) }
        }
    }
    fun autoActiveCancel() = CancelTask(autoActiveID)
    fun getAvailableNames(entity : E): List<String>{
        return skillList.filter { it.value.condition(entity) }.map { it.key }
    }
    fun getAvailableSkills(entity : E): List<Skill<E>>{
        return skillList.filter { it.value.condition(entity) }.map { it.value }
    }

}
class Skill<E : LivingEntity>(cooldown: Int, val skill: (E) -> Unit, val condition: (E) -> Boolean){
    lateinit var en : E
    val cool = CooldownWithTask(cooldown){
        debug("ran1")
        try {
            skill(en)
        }catch (e : Exception){
            e.printStackTrace()
        }

        debug("ran2")
    }
    fun checkConditionAndRun(entity: E): Boolean{
        en = entity
        return if(condition(entity)){
            cool.invoke()
        }else false
    }
}