package io.github.yoonseo.pastelplugin.rpg.quest

import org.bukkit.inventory.ItemStack

class ExecutableNode(val session : QuestSession){
    var code : (() -> Unit)?= null
    var giveItemCode : (() -> Unit)?= null
    fun code(code : () -> Unit){
        this.code = code
    }
    fun giveItem(item: ItemStack){
        giveItemCode = {
            session.quest.player.inventory.addItem(item)
        }
    }
    operator fun invoke(){
        code?.invoke()
        giveItemCode?.invoke()
    }
}