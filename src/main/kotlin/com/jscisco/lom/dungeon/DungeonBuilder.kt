package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.dungeon.strategies.GenerationStrategy
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D,
                     private val player: GameEntity<Player>,
                     private val strategy: GenerationStrategy = GenericDungeonStrategy(dungeonSize)) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    init {
        blocks = strategy.generateDungeon()
    }

    fun build(visibleSize: Size3D, dungeonSize: Size3D) = Dungeon(blocks, visibleSize, dungeonSize, player)

    private fun randomizeTiles() {
        forAllPositions { pos ->
            if (Math.random() > 0.9) {
                blocks[pos] = GameBlockFactory.wall()
            }
        }
    }

    private fun initializeFloors() {
        forAllPositions { pos ->
            blocks[pos] = GameBlockFactory.floor()
        }
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        dungeonSize.fetchPositions().forEach(fn)
    }

    private fun createOutsideWalls() {
        for (i in 0 until dungeonSize.xLength) {
            blocks[Position3D.create(i, 0, 0)] = GameBlockFactory.wall()
            blocks[Position3D.create(i, dungeonSize.yLength - 1, 0)] = GameBlockFactory.wall()
        }
        for (i in 0 until dungeonSize.yLength) {
            blocks[Position3D.create(0, i, 0)] = GameBlockFactory.wall()
            blocks[Position3D.create(dungeonSize.xLength - 1, i, 0)] = GameBlockFactory.wall()
        }
    }

}