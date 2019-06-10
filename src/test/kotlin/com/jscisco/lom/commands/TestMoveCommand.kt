package com.jscisco.lom.commands

import GameEntity
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.data.TestData
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.Test

class TestMoveCommand {

    private val testDungeon: Dungeon = TestData.newTestDungeon()
    private val context: GameContext = GameContext(dungeon = testDungeon,
            screen = mockk(),
            uiEvent = mockk())

    @Test
    fun `Moving to an unoccupied space should succeed`() {
        val player = testDungeon.player
        val newPosition = Position3D.create(1, 1, 0)
        val response = player.executeCommand(MoveCommand(context, player, newPosition))
        Assertions.assertThat(player.position).isEqualTo(newPosition)
        Assertions.assertThat(response).isEqualTo(Consumed)
    }

    @Test
    fun `Moving to an unwalkable space should fail`() {
        val player = testDungeon.player
        val startingPosition = Position3D.create(20, 20, 0)
        testDungeon.moveEntity(player, startingPosition)
        val newPosition = Position3D.create(6, 6, 0)
        testDungeon.fetchBlockAt(newPosition).ifPresent {
            it.addEntity(EntityFactory.newWall())
        }
        val response = player.executeCommand(MoveCommand(context, player, newPosition))
        Assertions.assertThat(response).isEqualTo(Pass)
        Assertions.assertThat(player.position).isEqualTo(startingPosition)
    }

    @Test
    fun `Moving to an occupied space should attack`() {
        val player: GameEntity<Player> = testDungeon.player
        val monster: GameEntity<NPC> = EntityFactory.newMonster()
        val monsterPos = Position3D.create(5, 5, 0)
        val initialMonsterHp = monster.findAttribute(Health::class).get().hp
        testDungeon.addEntity(monster, monsterPos)
        val response = player.executeCommand(MoveCommand(context, player, monsterPos))
        Assertions.assertThat(response).isEqualTo(Consumed)
        Assertions.assertThat(monster.findAttribute(Health::class).get().hp).isLessThan(initialMonsterHp)
    }


}