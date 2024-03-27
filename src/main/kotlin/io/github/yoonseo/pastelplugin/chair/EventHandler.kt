package io.github.yoonseo.pastelplugin.chair

import io.github.yoonseo.pastelplugin.debug
import io.github.yoonseo.pastelplugin.spawn
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.type.Stairs
import org.bukkit.entity.Boat
import org.bukkit.entity.SpectralArrow
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleExitEvent

class EventHandler : Listener {
    @EventHandler
    fun onInteraction(e : PlayerInteractEvent){
        if(e.action.isRightClick
            && e.clickedBlock?.type == Material.SPRUCE_STAIRS
            && e.clickedBlock?.blockData is Stairs
            && (e.clickedBlock?.blockData as Stairs).half.name == "BOTTOM"
            //&& (e.clickedBlock?.blockData as Stairs).shape == Stairs.Shape.STRAIGHT
            ){
            //debug((e.clickedBlock?.blockData as Stairs).half)
            e.clickedBlock!!.location.add(0.5,0.0,0.5).spawn(SpectralArrow::class).apply {
                addPassenger(e.player)
            }
        }
    }
}