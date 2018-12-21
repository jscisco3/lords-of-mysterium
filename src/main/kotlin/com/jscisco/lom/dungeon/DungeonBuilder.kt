package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.entities.Entity
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D,
                     private val hero: Entity = EntityFactory.newPlayer()) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()
//    private var dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 5)

    init {
        randomizeTiles()
    }

    fun build(visibleSize: Size3D, dungeonSize: Size3D) = Dungeon(blocks, visibleSize, dungeonSize, hero)

    private fun randomizeTiles() {
        forAllPositions { pos ->
            blocks[pos] = GameBlockFactory.floor()
        }
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        dungeonSize.fetchPositions().forEach(fn)
    }

}