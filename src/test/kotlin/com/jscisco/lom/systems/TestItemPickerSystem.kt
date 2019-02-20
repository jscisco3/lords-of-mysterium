package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.PickItemUpCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.extensions.position
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestItemPickerSystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private lateinit var dungeon: Dungeon
    private lateinit var player: GameEntity<Player>
    private lateinit var context: GameContext

    @BeforeEach
    fun init() {

        player = TEST_ITEM_PICKER()

        val dungeonSize = Size3D.create(100, 100, 2)
        dungeon = DungeonBuilder(
                dungeonSize = dungeonSize,
                strategy = EmptyDungeonStrategy(dungeonSize),
                player = player
        ).build(dungeonSize, dungeonSize)

        context = GameContext(
                dungeon = dungeon,
                screen = mockk(),
                player = player
        )
    }

    @Test
    fun `pick item up when item is present`() {
        val sword = TEST_SWORD
        val swordPosition = Position3D.create(5, 5, 0)
        dungeon.addEntity(sword, swordPosition)
        dungeon.moveEntity(player, swordPosition).toString()

        logger.info(player.position.toString())
        logger.info(sword.position.toString())

        player.executeCommand(PickItemUpCommand(
                context = context,
                source = player,
                position = player.position
        ))
        // There should be one item in the inventory
        Assertions.assertThat(player.inventory.items.size).isEqualTo(1)

    }

    @Test
    fun `pick item up when item is not present`() {
        player.executeCommand(PickItemUpCommand(
                context = context,
                source = player,
                position = player.position
        ))
        // The inventory should be empty!
        Assertions.assertThat(player.inventory.isEmpty).isTrue()
    }

    companion object {
        val TEST_SWORD = newGameEntityOfType(Item) {
            attributes(
                    EntityPosition()
            )
        }

        fun TEST_ITEM_PICKER() = newGameEntityOfType(Player) {
            attributes(
                    Inventory(maxWeight = 100),
                    EntityPosition()
            )
            facets(
                    ItemPickerSystem
            )
        }
    }
}