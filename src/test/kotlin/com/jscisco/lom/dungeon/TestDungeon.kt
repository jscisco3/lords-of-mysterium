package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.dungeon.strategies.GenerationStrategy
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.position
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.events.internal.ApplicationScope
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.builder.grid.TileGridBuilder
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.InputType
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.internal.Zircon
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestDungeon {

    val player: GameEntity<Player>
    val dungeon: Dungeon

    init {
        // Make sue we have no events subscriibed
        Zircon.eventBus.cancelScope(ApplicationScope)

        val dungeonSize: Size3D = Size3D.create(100, 60, 1)
        val visibleSize: Size3D = Size3D.create(25, 25, 1)
        player = EntityFactory.newPlayer()
        val strategy: GenerationStrategy = GenericDungeonStrategy(dungeonSize = dungeonSize)

        val dungeonBuilder: DungeonBuilder = DungeonBuilder(
                dungeonSize = dungeonSize,
                strategy = strategy,
                player = player
        )
        dungeon = dungeonBuilder.build(visibleSize, dungeonSize)

    }

    @Nested
    inner class DungeonMovement {

        private val initialPosition = Position3D.create(5, 5, 0)

        @BeforeEach
        fun init() {
            dungeon.moveEntity(player, initialPosition)
        }

        @Test
        fun testHandleInputMovementDown() {
            val keystroke = KeyStroke(
                    type = InputType.ArrowDown
            )

            val context = GameContext(
                    dungeon = dungeon,
                    screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
                    input = keystroke,
                    player = player
            )
            dungeon.handleInput(context)
            val expectedPosition = initialPosition.withRelativeY(1)
            Assertions.assertThat(player.position).isEqualTo(expectedPosition)
        }

        @Test
        fun testHandleInputMovementUp() {
            val keystroke = KeyStroke(
                    type = InputType.ArrowUp
            )

            val context = GameContext(
                    dungeon = dungeon,
                    screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
                    input = keystroke,
                    player = player
            )
            dungeon.handleInput(context)
            val expectedPosition = initialPosition.withRelativeY(-1)
            Assertions.assertThat(player.position).isEqualTo(expectedPosition)
        }

        @Test
        fun testHandleInputMovementLeft() {
            val keystroke = KeyStroke(
                    type = InputType.ArrowLeft
            )

            val context = GameContext(
                    dungeon = dungeon,
                    screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
                    input = keystroke,
                    player = player
            )
            dungeon.handleInput(context)
            val expectedPosition = initialPosition.withRelativeX(-1)
            Assertions.assertThat(player.position).isEqualTo(expectedPosition)
        }

        @Test
        fun testHandleInputMovementRight() {
            val keystroke = KeyStroke(
                    type = InputType.ArrowRight
            )

            val context = GameContext(
                    dungeon = dungeon,
                    screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
                    input = keystroke,
                    player = player
            )
            dungeon.handleInput(context)
            val expectedPosition = initialPosition.withRelativeX(1)
            Assertions.assertThat(player.position).isEqualTo(expectedPosition)
        }

        @Test
        fun testMoveEntitySuccessfully() {
            val newPosition = Position3D.create(20, 20, 0)
            val entityMoved = dungeon.moveEntity(player, newPosition)

            Assertions.assertThat(entityMoved).isTrue()
            Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(newPosition)
        }

        @Test
        fun testMoveEntityFailed() {
            val currentPosition = dungeon.findPositionOf(player).get()
            val newPosition = Position3D.create(-1, -1, 0)

            val entityMoved = dungeon.moveEntity(player, newPosition)
            Assertions.assertThat(entityMoved).isFalse()
            Assertions.assertThat(dungeon.findPositionOf(player).get()).isEqualTo(currentPosition)
        }

    }

    @Test
    fun testAddEntityAtPosition() {
        val sword = EntityFactory.newSword()
        val position = Position3D.create(15, 15, 0)
        dungeon.addEntity(sword, position)
        Assertions.assertThat(dungeon.findPositionOf(sword).get()).isEqualTo(Position3D.create(15, 15, 0))
    }

    @Test
    fun testAddDungeonEntity() {
        val sword = EntityFactory.newSword()
        dungeon.addDungeonEntity(sword)
        Assertions.assertThat(dungeon.findPositionOf(sword).isPresent).isFalse()
    }

    @Test
    fun testInitializeFOV() {
        Assertions.assertThat(dungeon.resistanceMap[0][0]).isEqualTo(1.0)
        Assertions.assertThat(dungeon.resistanceMap[1][1]).isEqualTo(0.0)
        Assertions.assertThat(dungeon.resistanceMap[99][59]).isEqualTo(1.0)
    }

}