package io.github.yoonseo.pastelplugin.skillHelper


import io.github.yoonseo.pastelplugin.spawn
import org.bukkit.block.Block
import org.bukkit.entity.Shulker
import org.bukkit.loot.LootTable
import org.bukkit.loot.LootTables

class GlowingBlock(block : Block) {
    companion object{
        fun removeGlow(block: Block){
            onGlowing[block]?.entity?.health = 0.0
            onGlowing.remove(block)
        }

        val onGlowing = HashMap<Block,GlowingBlock>()
    }
    val entity : Shulker
    init{
        onGlowing[block] = this
        entity = block.location.spawn(Shulker::class).apply {
            isGlowing = true
            setAI(false)
            isInvulnerable = true
            isInvisible = true
            lootTable = null
            isSilent = true
        }
    }



}
fun Block.glowing(boolean: Boolean){
    if(boolean){
        GlowingBlock(this)
    }else{
        GlowingBlock.removeGlow(this)
    }
}