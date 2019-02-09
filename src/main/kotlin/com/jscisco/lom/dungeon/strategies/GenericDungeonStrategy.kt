package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.FilledRectangleFactory
import squidpony.squidgrid.mapping.DungeonGenerator

class GenericDungeonStrategy(private val dungeonSize: Size3D) : GenerationStrategy(dungeonSize) {
    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        val dungeonGenerator = DungeonGenerator(dungeonSize.xLength, dungeonSize.yLength)
        for (z in 0 until dungeonSize.zLength) {
            dungeonGenerator.generate()
            dungeonGenerator.dungeon.forEachIndexed { row, arr ->
                arr.forEachIndexed { column, terrain ->
                    if (terrain == '#') {
                        blocks[Position3D.create(row, column, z)] = GameBlockFactory.wall()
                    } else {
                        blocks[Position3D.create(row, column, z)] = GameBlockFactory.floor()
                    }
                }
            }
            if (z > 0) {
                val stairsDown = dungeonGenerator.stairsDown
                blocks[Position3D.create(stairsDown.x, stairsDown.y, z)] = GameBlockFactory.stairsDown()
            }
            if (z < dungeonSize.zLength) {
                val stairsUp = dungeonGenerator.stairsUp
                blocks[Position3D.create(stairsUp.x, stairsUp.y, z)] = GameBlockFactory.stairsUp()
            }
        }
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