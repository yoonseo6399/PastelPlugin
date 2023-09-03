package io.github.yoonseo.pastelplugin.itemHandlers

import io.github.yoonseo.pastelplugin.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent


class EventHandler : Listener {
    companion object{
        private var list = ArrayList<Player>()
        private val hashmap = HashMap<Player,Int>()
        fun getClickingPlayer(player: Player)= list.contains(player)
    }
    @EventHandler
    fun onChangeItem(e: PlayerItemHeldEvent){

    }
    @EventHandler
    fun onInteraction(e: PlayerInteractEvent){
        if(e.action.isRightClick) process(e.player)
    }
    @EventHandler
    fun onEnInteraction(e: PlayerInteractEntityEvent){

        process(e.player)
    }

    private fun process(player: Player){

        hashmap[player] = 5

        //System - keepClicking
        list.add(player)


        ScheduleRepeating(taskName = "InteractionProcess#${player.name}") {
            hashmap[player] = hashmap[player]!!-1
            //player.sendDebugMessage("taskID: $it, discount : ${hashmap[player]!!}")
            if(hashmap[player]!!<=0){
                list = list.filter { it.name != player.name } as ArrayList<Player>
                //player.sendDebugMessage("taskID: $it, now : ${hashmap[player]!!}, Stopped , list : ${list.toString()}")
                CancelTask("InteractionProcess#${player.name}")
            }
        }

    }

}
val Player.isKeepClicking : Boolean
    get() = io.github.yoonseo.pastelplugin.itemHandlers.EventHandler.getClickingPlayer(this)