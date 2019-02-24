package com.jscisco.lom.dungeon

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.data.TestData
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.position
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TestDungeon {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val dungeon: Dungeon = TestData.newTestDungeon()
    val player = dungeon.player

    @Nested
    inner class DungeonMovement {

        private val initialPosition = Position3D.create(5, 5, 0)

        @BeforeEach
        fun init() {
            dungeon.moveEntity(player, initialPosition)
        }

        @Test
        fun `move an entity to an unoccupied position should succeed`() {
            val newPosition = Position3D.create(20, 20, 0)
            val previousGameBlock = dungeon.fetchBlockAt(player.position).get()
            val entityMoved = dungeon.moveEntity(player, newPosition)
            Assertions.assertThat(entityMoved).isTrue()
            // The game block that held the player should no longer
            Assertions.assertThat(previousGameBlock.entities.contains(player)).isFalse()
            Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(newPosition)
            // The new game block should have the entity
            Assertions.assertThat(dungeon.fetchBlockAt(newPosition).get().entities.contains(player)).isTrue()
        }

        @Test
        fun `moving an entity to an unoccupied position should fail`() {
            val currentPosition = dungeon.findPositionOf(player).get()
            val newPosition = Position3D.create(-1, -1, 0)

            val entityMoved = dungeon.moveEntity(player, newPosition)
            Assertions.assertThat(entityMoved).isFalse()
            Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(currentPosition)
        }

    }

    @Test
    fun `Entities added at a particular location should be there`() {
        val sword = EntityFactory.newSword()
        val position = Position3D.create(15, 15, 0)
        dungeon.addEntity(sword, position)
        Assertions.assertThat(dungeon.findPositionOf(sword).get()).isEqualTo(Position3D.create(15, 15, 0))
    }

    @Test
    fun `Dungeon entities (those with no position) should not be present`() {
        val sword = EntityFactory.newSword()
        dungeon.addDungeonEntity(sword)
        Assertions.assertThat(dungeon.findPositionOf(sword).isPresent).isFalse()
    }

    @Test
    fun `The resistance map should be calculate as expected`() {
        val wallPosition = Position3D.create(10, 10, 0)
        dungeon.addEntity(EntityFactory.newWall(), wallPosition)

        dungeon.calculateResistanceMap(dungeon.resistanceMap)

        val firstFloorResistanceMap = dungeon.resistanceMap[0]

        Assertions.assertThat(firstFloorResistanceMap).isNotNull
        if (firstFloorResistanceMap != null) {
            Assertions.assertThat(firstFloorResistanceMap[10][10]).isEqualTo(1.0)
            Assertions.assertThat(firstFloorResistanceMap[0][0]).isEqualTo(0.0)
        }

    }

}