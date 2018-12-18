package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D, val gridSize: Size) {

    // the dungeon size is XYZ where Z is depth
    private val width = dungeonSize.xLength
    private val height = dungeonSize.yLength
    private var startingBlocks: MutableMap<Position3D, GameBlock> = mutableMapOf()
    // TBD
    private var nextRegion: Int = 0
    val visibleSize = Sizes.create3DSize(gridSize.width / 5 * 4, gridSize.height / 6 * 5, 1)


    fun build(): Dungeon = Dungeon(startingBlocks, visibleSize, dungeonSize)

}