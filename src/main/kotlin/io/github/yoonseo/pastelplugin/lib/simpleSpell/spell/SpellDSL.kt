package io.github.yoonseo.pastelplugin.lib.simpleSpell.spell

import HomingObject
import io.github.yoonseo.pastelplugin.getOverworldLocation
import io.github.yoonseo.pastelplugin.rpg.magic.RedGloom.Maline
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action

class TopSpellInitializer{
    fun name(name: Component){
        TODO()
    }
    fun activator(activator : SpellActivator){
        TODO()
    }
    fun action(block : SpellActor.() -> Unit){
        TODO()
    }
}
class SpellActor{
    lateinit var caster : Player
    lateinit var oBlock : ObjectActor.() -> Unit
    fun Object(block: ObjectActor.() -> Unit) {
        oBlock = block
    }
    fun act(player: Player){
        caster = player
        val o = SpellObject(getOverworldLocation(0,0,0))
        oBlock(ObjectActor(o,this))
    }
}

data class SpellObject(var location : Location,var target: LivingEntity? = null){
    val behaviors = ArrayList<ObjectBehavior>()

}


class ObjectActor(val o : SpellObject, sa : SpellActor){
    val caster = sa.caster

    fun addBehavior(behavior: ObjectBehavior){
        o.behaviors.add(behavior)
    }
    fun getBehaviors() = o.behaviors




}
fun t(){
    with(TopSpellInitializer()) {
        name(Component.text("a"))
        activator(SpellActivator.InteractionWithItem(Action.LEFT_CLICK_BLOCK,Maline()))
        action {
            Object {
                o.location = caster.location.clone()

                addBehavior()
                addBehavior()
            }
        }
    }
}
abstract class ObjectBehavior{
    abstract val objact : SpellObject
    abstract fun register()
    abstract fun cancel()

}

class Homing(ob : SpellObject,val target : LivingEntity) : ObjectBehavior(){
    override val objact: SpellObject = ob
    lateinit var homingObject: HomingObject

    init {
        HomingObject(objact.location,target)
    }
    override fun register() {
        HomingObject(objact.location, target)
    }

}
