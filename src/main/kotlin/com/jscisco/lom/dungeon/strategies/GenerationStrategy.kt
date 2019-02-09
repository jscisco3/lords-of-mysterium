package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import java.io.File

abstract class GenerationStrategy(private val dungeonSize: Size3D) {

    var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    abstract fun generateDungeon(): MutableMap<Position3D, GameBlock>

    internal fun initializeOutsideWalls() {
        for (z in 0 until dungeonSize.zLength) {
            for (i in 0 until dungeonSize.xLength) {
                blocks[Position3D.create(i, 0, z)] = GameBlockFactory.wall()
                blocks[Position3D.create(i, dungeonSize.yLength - 1, z)] = GameBlockFactory.wall()
            }
            for (i in 0 until dungeonSize.yLength) {
                blocks[Position3D.create(0, i, z)] = GameBlockFactory.wall()
                blocks[Position3D.create(dungeonSize.xLength - 1, i, z)] = GameBlockFactory.wall()
            }
        }

    }

    internal fun randomizeTiles() {
        forAllPositions { pos ->
            if (Math.random() > 0.9) {
                blocks[pos] = GameBlockFactory.wall()
            }
        }
    }

    internal fun initializeFloors() {
        forAllPositions { pos ->
            blocks[pos] = GameBlockFactory.floor()
        }
    }

    internal fun initializeWalls() {
        forAllPositions { pos ->
            blocks[pos] = GameBlockFactory.wall()
        }
    }

    open fun createItems() {
        blocks[Position3D.create(5, 5, 0)] = GameBlock.createWith(EntityFactory.newSword())
    }

    internal fun placeStairs() {
        blocks[Position3D.create(15, 15, 0)] = GameBlock.createWith(EntityFactory.stairsUp())
        blocks[Position3D.create(20, 30, 1)] = GameBlock.createWith(EntityFactory.stairsDown())

    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        dungeonSize.fetchPositions().forEach(fn)
    }

}