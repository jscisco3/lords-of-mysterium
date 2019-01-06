package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.dungeon.DungeonBuilder
import org.assertj.core.api.Assertions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.Test

class TestEmptyDungeonGeneration {

    @Test
    fun testEmptyDungeonCreated() {
        val dungeonSize: Size3D = Size3D.create(100, 100, 1)
        val dungeon = DungeonBuilder(dungeonSize = dungeonSize,
                strategy = EmptyDungeonStrategy(dungeonSize),
                player = EntityFactory.newPlayer()).build(dungeonSize, dungeonSize)

        val topLeftCorner = dungeon.fetchBlockAt(Position3D.create(0, 0, 0))

        Assertions.assertThat(topLeftCorner.get().isOccupied).isTrue()
        Assertions.assertThat(topLeftCorner.get().isWall).isTrue()
    }
}