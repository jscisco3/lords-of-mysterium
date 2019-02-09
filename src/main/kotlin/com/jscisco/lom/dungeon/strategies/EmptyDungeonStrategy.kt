package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class EmptyDungeonStrategy(private val dungeonSize: Size3D) : GenerationStrategy(dungeonSize) {
    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        initializeFloors()
        initializeOutsideWalls()
        createItems()
        placeStairs()

        blocks[Position3D.create(7, 7, 0)]?.addEntity(EntityFactory.newClosedDoor())

        return blocks
    }
}