package com.jscisco.lom.systems

import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.Generic
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.newGameEntityOfType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestMoveSystem {

    val mockDungeon = mockk<Dungeon>()
    val context = GameContext(
            dungeon = mockDungeon,
            screen = mockk(),
            player = mockk()
    )

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        every { mockDungeon.fetchBlockAt(Position3D.create(1, 1, 0)) } returns Maybe.of(GameBlockFactory.floor())
        every { mockDungeon.moveEntity(any(), any()) } returns true
    }

    @Test
    fun `If I move successfully, I should remove the ActiveTurn attribute if present`() {
        entityWithTurn.executeCommand(MoveCommand(context, entityWithTurn, Position3D.create(1, 1, 0)))
        Assertions.assertThat(entityWithTurn.hasAttribute<ActiveTurn>()).isFalse()
    }

    @Test
    fun `If I move successfully, I will remove the ActiveTurn attribute even if not present`() {
        entityWithoutTurn.executeCommand(MoveCommand(context, entityWithoutTurn, Position3D.create(1, 1, 0)))
        Assertions.assertThat(entityWithoutTurn.hasAttribute<ActiveTurn>()).isFalse()
    }

    private val entityWithTurn = newGameEntityOfType(Generic) {
        attributes(
                ActiveTurn
        )
        facets(MoveSystem)
    }

    private val entityWithoutTurn = newGameEntityOfType(Generic) {
        attributes(
        )
        facets(MoveSystem)
    }
}