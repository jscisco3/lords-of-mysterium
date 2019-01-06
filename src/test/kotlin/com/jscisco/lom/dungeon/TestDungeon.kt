package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.dungeon.strategies.GenerationStrategy
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import org.assertj.core.api.Assertions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.Test

class TestDungeon {

    val dungeonSize: Size3D = Size3D.create(100, 100, 1)
    val visibleSize: Size3D = Size3D.create(25, 25, 1)
    val player: GameEntity<Player> = EntityFactory.newPlayer()
    val strategy: GenerationStrategy = GenericDungeonStrategy(dungeonSize = dungeonSize)

    val dungeonBuilder: DungeonBuilder = DungeonBuilder(
            dungeonSize = dungeonSize,
            strategy = strategy,
            player = player
    )
    val dungeon = dungeonBuilder.build(visibleSize, dungeonSize)

    @Test
    fun testAddEntityAtPosition() {
        val sword = EntityFactory.newSword()
        val position = Position3D.create(15, 15, 0)
        dungeon.addEntity(sword, position)

        Assertions.assertThat(dungeon.findPositionOf(sword).get()).isEqualTo(Position3D.create(15, 15, 0))
    }

    @Test
    fun testAddDungeonEntity() {
        val dungeon = dungeonBuilder.build(visibleSize, dungeonSize)
        val sword = EntityFactory.newSword()
        dungeon.addDungeonEntity(sword)

        Assertions.assertThat(dungeon.findPositionOf(sword).isPresent).isFalse()
    }

    @Test
    fun testMoveEntitySuccessfully() {
        dungeon.addEntity(player, Position3D.defaultPosition())

        val newPosition = Position3D.create(20, 20, 0)
        val entityMoved = dungeon.moveEntity(player, newPosition)

        Assertions.assertThat(entityMoved).isTrue()
        Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(newPosition)
    }

    @Test
    fun testMoveEntityFailed() {
        dungeon.addEntity(player, Position3D.defaultPosition())

        val currentPosition = dungeon.findPositionOf(player).get()
        val newPosition = Position3D.create(-1, -1, 0)


        val entityMoved = dungeon.moveEntity(player, newPosition)
        Assertions.assertThat(entityMoved).isFalse()
        Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(currentPosition)


    }


}