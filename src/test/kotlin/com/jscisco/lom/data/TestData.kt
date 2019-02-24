package com.jscisco.lom.data

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

object TestData {

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

        val dungeon = Dungeon(blocks = blocks,
                visibleSize = Size3D.create(25, 25, 1),
                actualSize = dungeonSize,
                player = EntityFactory.newPlayer()
        )

        return dungeon
    }

}