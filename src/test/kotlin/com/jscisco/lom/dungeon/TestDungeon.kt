package com.jscisco.lom.dungeon

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.data.TestData
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
            Assertions.assertThat(previousGameBlock.entities.toList().contains(player)).isFalse()
            Assertions.assertThat(player.position).isEqualTo(newPosition)
            // The new game block should have the entity
            Assertions.assertThat(dungeon.fetchBlockAt(newPosition).get().entities.toList().contains(player)).isTrue()
        }

        @Test
        fun `moving an entity to an unoccupied position should fail`() {
            val currentPosition = dungeon.player.position
            val newPosition = Position3D.create(-1, -1, 0)

            val entityMoved = dungeon.moveEntity(player, newPosition)
            Assertions.assertThat(entityMoved).isFalse()
            Assertions.assertThat(dungeon.player.position).isEqualTo(currentPosition)
        }

    }

    @Test
    fun `Entities added at a particular location should be there`() {
        val goblin = EntityFactory.newMonster()
        val position = Position3D.create(15, 15, 0)
        dungeon.addEntity(goblin, position)
        Assertions.assertThat(dungeon.fetchBlockAt(position).get().entities.contains(goblin)).isTrue()
        Assertions.assertThat(goblin.position).isEqualTo(position)
    }

//    @Nested
//    inner class FOVTests {
//        @Test
//        fun `The resistance map should be calculate as expected`() {
//            val wallPosition = Position3D.create(10, 10, 0)
//            dungeon.fetchBlockAt(wallPosition).ifPresent {
//                it.addEntity(EntityFactory.newWall())
//            }
//
//            dungeon.calculateResistanceMap(dungeon.player)
//
//            val firstFloorResistanceMap = dungeon.calculateResistanceMap(dungeon.player)
//
//            Assertions.assertThat(firstFloorResistanceMap).isNotNull
//            if (firstFloorResistanceMap != null) {
//                Assertions.assertThat(firstFloorResistanceMap[10][10]).isEqualTo(1.0)
//                Assertions.assertThat(firstFloorResistanceMap[0][0]).isEqualTo(0.0)
//            }
//        }

//        @Test
//        fun `Game block in FOV should have in_fov toggled & seen`() {
//            dungeon.player.fieldOfView.fov[0][0] = 1.0
//            dungeon.updateGameBlocks(dungeon.player)
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().inFov).isTrue()
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().seen).isTrue()
//        }
//
//        @Test
//        fun `Game block not in FOV should not be inFov`() {
//            dungeon.player.fieldOfView.fov[0][0] = 0.0
//            dungeon.updateGameBlocks(dungeon.player)
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().inFov).isFalse()
//        }
//
//        @Test
//        fun `Game block that was in FOV but is no longer should be seen`() {
//            dungeon.player.fieldOfView.fov[0][0] = 1.0
//            dungeon.updateGameBlocks(dungeon.player)
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().inFov).isTrue()
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().seen).isTrue()
//            dungeon.player.fieldOfView.fov[0][0] = 0.0
//            dungeon.updateGameBlocks(dungeon.player)
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().inFov).isFalse()
//            Assertions.assertThat(dungeon.fetchBlockAt(Position3D.create(0, 0, 0)).get().seen).isTrue()
//        }

//    }
}