package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

abstract class GenerationStrategy(private val size: Size3D) {

    var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    abstract fun generateDungeon(): MutableMap<Position3D, GameBlock>

    internal fun createOutsideWalls() {
        for (i in 0 until size.xLength) {
            blocks[Position3D.create(i, 0, 0)] = GameBlockFactory.wall()
            blocks[Position3D.create(i, size.yLength - 1, 0)] = GameBlockFactory.wall()
        }
        for (i in 0 until size.yLength) {
            blocks[Position3D.create(0, i, 0)] = GameBlockFactory.wall()
            blocks[Position3D.create(size.xLength - 1, i, 0)] = GameBlockFactory.wall()
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

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        size.fetchPositions().forEach(fn)
    }

}