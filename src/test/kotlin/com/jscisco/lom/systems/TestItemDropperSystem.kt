package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.DropItemCommand
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

class TestItemDropperSystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private lateinit var dungeon: Dungeon
    private val player: GameEntity<Player> = TEST_ITEM_DROPPER
    private lateinit var context: GameContext

    @BeforeEach
    fun init() {

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
    fun `drop an item if it is in the dropper's inventory`() {
        val sword = TEST_SWORD()
        player.inventory.addItem(sword)
        Assertions.assertThat(player.inventory.items).contains(sword)

        player.executeCommand(DropItemCommand(
                context = context,
                source = player,
                item = sword,
                position = player.position
        ))

        Assertions.assertThat(player.inventory.isEmpty).isTrue()
        Assertions.assertThat(sword.position).isEqualTo(player.position)

    }

    @Test
    fun `don't do anything if the item is not in the dropper's inventory`() {
        val sword = TEST_SWORD()
        Assertions.assertThat(sword.position).isEqualTo(Position3D.unknown())
        player.executeCommand(DropItemCommand(
                context = context,
                source = player,
                item = sword,
                position = player.position
        ))

        Assertions.assertThat(player.inventory.isEmpty).isTrue()
        Assertions.assertThat(sword.position).isEqualTo(Position3D.unknown())
    }

    companion object {
        fun TEST_SWORD() = newGameEntityOfType(Item) {
            attributes(
                    EntityPosition()
            )
        }

        val TEST_ITEM_DROPPER = newGameEntityOfType(Player) {
            attributes(
                    Inventory(maxWeight = 100),
                    EntityPosition()
            )
            facets(
                    ItemDropperSystem
            )
        }
    }

}