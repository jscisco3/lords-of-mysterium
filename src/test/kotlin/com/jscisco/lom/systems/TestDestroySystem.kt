package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.commands.DestroyCommand
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import io.mockk.mockk
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.jupiter.api.Test

class TestDestroySystem {

    private val player: GameEntity<Player> = mockk()
    private val dungeonSize = Size3D.create(1, 1, 0)
    private val dungeon = DungeonBuilder(
            dungeonSize = dungeonSize,
            player = player,
            strategy = mockk()
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
        val destroyable = destroyableEntity()
        val position = Position3D.create(1, 1, 0)
        dungeon.addEntity(destroyable, position)
        destroyable.sendCommand(DestroyCommand(
                mockk(),
                destroyable,
                ""
        ))
    }

    companion object {
        fun destroyableEntity() = newGameEntityOfType(NPC) {
            attributes()
            facets(DestroySystem)
        }
    }

}