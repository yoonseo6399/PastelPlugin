package io.github.yoonseo.pastelplugin.itemHandlers

import io.github.yoonseo.pastelplugin.isNamed
import io.github.yoonseo.pastelplugin.itemHandlers.Requires.*
import io.github.yoonseo.pastelplugin.system.register
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import java.lang.RuntimeException


abstract class AbstractCustomItem : CustomItem {
    init {
        register()
    }

    override val lores: ArrayList<Component?> = ArrayList()


    val interactEventList = ArrayList<AdvancedInteractConditions.(PlayerInteractEvent) -> Unit>()
    val hotbarItemChangeEventList = ArrayList<(PlayerItemHeldEvent) -> Unit>()
    override fun playerInteractionEvent(advancedInteractConditions: AdvancedInteractConditions.(PlayerInteractEvent) -> Unit) {
        interactEventList.add(advancedInteractConditions)
    }

    override fun playerItemChangeToOtherItemEvent(code: (PlayerItemHeldEvent) -> Unit) {
        hotbarItemChangeEventList.add(code)
    }
    @EventHandler
    fun interact(e: PlayerInteractEvent){
        for (interactEvent in interactEventList){
            try {
                interactEvent(AdvancedInteractConditions(this,e),e)
            }catch (_: RequirementDenied){ }

        }
    }


    @EventHandler
    fun changeHotbarItem(e: PlayerItemHeldEvent){
        for (event in hotbarItemChangeEventList){
            if(e.player.inventory.getItem(e.previousSlot)?.displayName() == name &&
                    e.player.inventory.getItem(e.newSlot)?.displayName() != name)
            {
                event(e)
            }
        }
    }

    fun getItem(): ItemStack{
        return ItemStack(itemType,1).apply {
            val meta = itemMeta
            meta.displayName(name)
            meta.lore(lores)
            itemMeta = meta
        }
    }

}
class RequirementDenied(msg: String): RuntimeException(msg)

interface CustomItem : Listener{
    val name : TextComponent
    val lores : ArrayList<Component?>
    val itemType : Material
    fun playerInteractionEvent(advancedInteractConditions: AdvancedInteractConditions.(PlayerInteractEvent) -> Unit)
    fun playerItemChangeToOtherItemEvent(code: (PlayerItemHeldEvent) -> Unit)
}

class AdvancedInteractConditions(val i: CustomItem, val e: PlayerInteractEvent){
    val player = e.player
    val playerLocation = e.player.location



    /**
    @throws RuntimeException to stop code( corutines
     **/
    fun require(requires: Requires) {
        if(
                !when(requires){
                    LEFT_CLICK -> {
                        e.action.isLeftClick
                    }
                    RIGHT_CLICK -> {
                        e.action.isRightClick
                    }
                    THIS_ITEM -> {
                        e.player.equipment.itemInMainHand.itemMeta?.displayName() == i.name
                    }
                    THIS_ITEM_NAME -> {
                        e.player.equipment.itemInMainHand isNamed i.name.content()
                    }
                }
        ){//in  if(!when(){})
            throw RequirementDenied("required : ${requires.name}, if you see this message, Something went wrong, pls use try-catch on your code")
        }
    }
}
enum class Requires{
    LEFT_CLICK,RIGHT_CLICK,
    THIS_ITEM,THIS_ITEM_NAME
}