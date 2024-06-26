package io.github.yoonseo.pastelplugin.lib.simpleSpell.spell

import io.github.yoonseo.pastelplugin.itemHandlers.CustomItem
import org.bukkit.entity.Player

interface Spell {
    var activator : SpellActivator
    var action : SpellAction
    companion object{
        operator fun invoke()
    }
    fun activate(caster : Player): ActivatedSpell
}


open class SpellImpl : Spell{
    lateinit var attitude



    fun activate(caster : Player){

    }



}
class ActivatedSpell(upperSpell: Spell,player: Player) : Spell by upperSpell{

}

class SpellAction{

}
class SpellActivator(val block : () -> Player?){

    companion object {
        fun InteractionWithItem(action : org.bukkit.event.block.Action,item : CustomItem) : SpellActivator{
            var result : Player? = null
            item.playerInteractionEvent { if(e.action == action) result = this.player }
            return create{
                result
            }
        }
        fun create(block : () -> Player?) : SpellActivator{
            return SpellActivator(block)
        }
    }
    fun check() : Player?{
        return block()
    }

}