package com.jscisco.lom.data

import GameEntity
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.extensions.addAtEmptyPosition
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

object TestData {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)


    fun newTestDungeon(): Dungeon {
        val dungeonSize = Size3D.create(100, 100, 5)

        val blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

        for (i in 0 until dungeonSize.xLength) {
            for (j in 0 until dungeonSize.yLength) {
                for (k in 0 until dungeonSize.zLength) {
                    blocks[Position3D.create(i, j, k)] = GameBlockFactory.floor()
                }
            }
        }

        return Dungeon(blocks = blocks,
                visibleSize = Size3D.create(25, 25, 1),
                actualSize = dungeonSize).also {
            it.addAtEmptyPosition(newPlayer())
        }
    }

    fun newPlayer(): GameEntity<Player> {
        return EntityFactory.newPlayer()
    }

}