package io.github.yoonseo.pastelplugin.boss


import io.github.yoonseo.pastelplugin.*
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.EnumSet
import kotlin.reflect.KClass



abstract class Boss(bossName : String){
    companion object{
        val list = HashMap<String,Boss>()
        fun spawn(bossName: String,loc: Location): Boss? {
            return list[bossName]?.spawn(loc)
        }
    }
    lateinit var mainEntity : LivingEntity

    init{
        list[bossName] = this
    }

    abstract fun spawn(loc : Location) : Boss

}


open class CustomEntity<E : LivingEntity> protected constructor(private val clazz: KClass<E>){
    var isSpawned = false
    var hasCustomAI = false
    var customAI : CustomAI<*>? = null
    private var onDeath : () -> Unit = {}
    private var onSpawn : () -> Unit = {}


    private var entityInit : (E.() -> Unit)? = null
    private lateinit var entity : E

    operator fun invoke(): E = if (isSpawned) entity else spawn(overworld.spawnLocation).also { debug("intance invoke") }
    companion object{
        private val entityList = HashMap<KClass<*>,CustomEntity<*>>()

        operator fun <E : CustomEntity<*>> invoke(clazz : KClass<E>): E = clazz.constructors.first().call()
    }

    init{
        entityList[this::class] = this
    }

    fun spawn(location: Location): E{
        entity = location.spawn(clazz)
        entityInit?.invoke(entity)
        isSpawned = true
        onSpawn()
        return entity
    }

    fun OnSpawn(task: () -> Unit){
        onSpawn = task
    }
    fun OnDeath(task: () -> Unit){
        onDeath = task
        ScheduleRepeating {
            if(entity.isDead){
                onDeath()
                CancelTask(it)
            }
        }
    }

    fun entityInit(init : (E.() -> Unit)) { entityInit = init }




}
