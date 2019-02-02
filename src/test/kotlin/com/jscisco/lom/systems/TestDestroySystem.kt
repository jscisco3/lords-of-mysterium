package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.commands.DestroyCommand
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
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.jupiter.api.Test

class TestDestroySystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val player: GameEntity<Player> = destroyableEntity()
    private val dungeonSize = Size3D.create(15, 15, 1)
    private val dungeon = DungeonBuilder(
            dungeonSize = dungeonSize,
            player = player,
            strategy = EmptyDungeonStrategy(dungeonSize)
    ).build(
            dungeonSize,
            dungeonSize
    )

    private val context: GameContext = GameContext(
            dungeon = dungeon,
            player = player,
            input = mockk(),
            screen = mockk()
    )

    @Test
    fun `destroy command should remove an entity if it is present in the dungeon`() {
        player.executeCommand(DestroyCommand(
                context,
                player,
                ""
        ))
        Assertions.assertThat(dungeon.fetchEntitiesAt(player.position).isEmpty()).isTrue()
    }

    companion object {
        fun destroyableEntity() = newGameEntityOfType(Player) {
            attributes(
                    EntityPosition()
            )
            facets(DestroySystem)
        }
    }

}