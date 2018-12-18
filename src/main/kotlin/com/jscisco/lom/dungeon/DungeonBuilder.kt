package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockRepository
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D, val gridSize: Size) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    // the dungeon size is XYZ where Z is depth
    private val width = dungeonSize.xLength
    private val height = dungeonSize.yLength
    private var startingBlocks: MutableMap<Position3D, GameBlock> = mutableMapOf()
    // TBD
    private var nextRegion: Int = 0
    val visibleSize = Sizes.create3DSize(gridSize.width / 5 * 4, gridSize.height / 6 * 5, 1)


    fun build(): Dungeon {
//        randomizeTiles()
        startingBlocks.set(Position3D.create(0, 0, 1), GameBlockRepository.wall())
        logger.info(startingBlocks.get(Position3D.create(0, 0, 1)).toString())
        return Dungeon(startingBlocks, visibleSize, dungeonSize)
    }

    private fun randomizeTiles() {
        forAllPositions { pos ->
            startingBlocks[pos] = if (Math.random() < 0.5) {
                logger.info("Creating a wall at %s".format(pos.toString()))
                GameBlockRepository.wall()
            } else {
                GameBlockRepository.floor()
            }
        }
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        dungeonSize.fetchPositions().forEach(fn)
    }

}