package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.FilledRectangleFactory

class GenericDungeonStrategy(private val dungeonSize: Size3D) : GenerationStrategy(dungeonSize) {
    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        initializeWalls()
        digRooms(10)
        initializeOutsideWalls()
        writeDungeonToFile()
        return blocks
    }

    fun digRooms(numRooms: Int) {
        for (z in 0 until dungeonSize.zLength) {
            FilledRectangleFactory.buildFilledRectangle(Position.create(1, 1), Sizes.create(5, 5)).forEach {
                blocks[Position3D.from2DPosition(it, z)] = GameBlockFactory.floor()
            }
            EllipseFactory.buildEllipse(Position.create(15, 15), Position.create(20, 20)).forEach {
                blocks[Position3D.from2DPosition(it, z)] = GameBlockFactory.floor()
            }
        }
    }

}