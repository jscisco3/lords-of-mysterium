package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class EmptyDungeonStrategy(private val size: Size3D) : GenerationStrategy(size) {
    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        initializeFloors()
        createOutsideWalls()
        createItems()
        return blocks
    }
}