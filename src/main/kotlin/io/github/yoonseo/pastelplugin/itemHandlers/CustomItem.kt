package io.github.yoonseo.pastelplugin.itemHandlers

import io.github.yoonseo.pastelplugin.ComponentNamedWith
import io.github.yoonseo.pastelplugin.ScheduleRepeating
import io.github.yoonseo.pastelplugin.isNamed
import io.github.yoonseo.pastelplugin.itemHandlers.Requires.*
import io.github.yoonseo.pastelplugin.skillHelper.Selector
import io.github.yoonseo.pastelplugin.system.register
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import java.lang.RuntimeException


abstract class AbstractCustomItem : CustomItem {
    companion object{
        val list = ArrayList<AbstractCustomItem>()
    }
    init {
        register()
        list.add(this)
    }

    override val lores: ArrayList<Component?> = ArrayList()

    val listeners = ArrayList<(PlayerItemHeldEvent) -> Unit>();
    val interactEventList = ArrayList<AdvancedInteractConditions.(PlayerInteractEvent) -> Unit>()
    val hotbarItemChangeEventList = ArrayList<(PlayerItemHeldEvent) -> Unit>()

    override var cooldown = 0


    override fun playerInteractionEvent(advancedInteractConditions: AdvancedInteractConditions.(PlayerInteractEvent) -> Unit) {
        interactEventList.add(advancedInteractConditions)
    }

    override fun playerItemChangeToOtherItemEvent(code: (PlayerItemHeldEvent) -> Unit) {
        hotbarItemChangeEventList.add(code)
    }

    fun whenSelected(action: (PlayerItemHeldEvent) -> Unit){
        listeners.add(action)
    }

    @EventHandler
    fun eventHandler(e: PlayerItemHeldEvent){
        val nowItem = e.player.inventory.getItem(e.newSlot) ?: return
        val previousItem = e.player.inventory.getItem(e.previousSlot)
        if(nowItem ComponentNamedWith name){
            listeners.forEach{
                it.invoke(e)
            }
        }
        for (event in hotbarItemChangeEventList){

            if (previousItem != null) {
                if(!(nowItem ComponentNamedWith name) &&
                        previousItem ComponentNamedWith name)
                {
                    event(e)
                }
            }
        }
    }
    @EventHandler
    fun interact(e: PlayerInteractEvent){
        for (interactEvent in interactEventList){
            try {
                interactEvent(AdvancedInteractConditions(this,e),e)
            }catch (_: RequirementDenied){ }
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
    var cooldown: Int
    fun playerInteractionEvent(advancedInteractConditions: AdvancedInteractConditions.(PlayerInteractEvent) -> Unit)
    fun playerItemChangeToOtherItemEvent(code: (PlayerItemHeldEvent) -> Unit)
}

class AdvancedInteractConditions(val i: CustomItem, val e: PlayerInteractEvent){
    val player = e.player
    val playerLocation = e.player.location
    val direction = playerLocation.direction
    val target : LivingEntity by lazy { getTargetLegacy() ?: throw IllegalAccessException("Target required") }

    private fun getTargetLegacy() = Selector(100).selectLivingEntity(player.eyeLocation) {it != player}?.firstOrNull()

    /**
    @throws RuntimeException to stop code
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
                    TARGET -> {
                        getTargetLegacy() != null
                    }
                }
        ){//in  if(!when(){})
            stopCode()
        }
    }
    fun cooldown(tick: Int){
        if(i.cooldown <= 0){
            i.cooldown = tick
            ScheduleRepeating(expireTick = tick.toLong()) { i.cooldown -- }
        }else stopCode()
    }

    private fun stopCode(): Nothing = throw RequirementDenied("if you see this message, Something went wrong, pls use try-catch on your code")
}
enum class Requires{
    LEFT_CLICK,RIGHT_CLICK,
    THIS_ITEM,THIS_ITEM_NAME,TARGET
}