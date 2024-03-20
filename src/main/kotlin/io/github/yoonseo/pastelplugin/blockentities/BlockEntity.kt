package io.github.yoonseo.pastelplugin.blockentities

import io.github.yoonseo.pastelplugin.spawn
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.BlockDisplay

class BlockEntity (val e : BlockDisplay): BlockDisplay by e{
    constructor(block: BlockData, location : Location) : this(location.spawn(BlockDisplay::class).apply {
        setBlock(block)
    })


}
