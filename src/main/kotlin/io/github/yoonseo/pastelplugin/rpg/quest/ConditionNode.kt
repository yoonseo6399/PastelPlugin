package io.github.yoonseo.pastelplugin.rpg.quest

import io.github.yoonseo.pastelplugin.approachAt
import io.github.yoonseo.pastelplugin.rpg.moster.Monster
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class ConditionNode {
    var obtain: (Player.(Quest)->Boolean) = { true }
    var approachAt: (Player.(Quest)->Boolean) = { true }
    var kill: (Player.(Quest)->Boolean) = { true }
    fun obtain(item : ItemStack,amount: Int){
        obtain = {
            inventory.containsAtLeast(item,amount)
        }
    }
    fun approachAt(loc: Location,distance: Float){
        approachAt = {
            approachAt(loc,distance)
        }
    }
    fun kill(monster: Monster<*>,amount: Int){
        TODO()
    }
    val condition : Player.(Quest)->Boolean
        get() = { obtain(this,it) && approachAt(this,it) && kill(this,it) }
    operator fun invoke(player: Player,quest: Quest) = condition(player,quest)
}


