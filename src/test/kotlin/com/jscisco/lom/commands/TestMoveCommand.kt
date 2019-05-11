package com.jscisco.lom.commands

import com.jscisco.lom.actor.Monster
import com.jscisco.lom.actor.Player
import com.jscisco.lom.data.TestData
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.terrain.Wall
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.Test

class TestMoveCommand {

    private val testDungeon: Dungeon = TestData.newTestDungeon().apply {
        moveEntity(this.player, Position3D.create(10, 10, 0))
    }

    @Test
    fun `Moving to an unoccupied space should succeed`() {
        val player = testDungeon.player
        val newPosition = Position3D.create(1, 1, 0)
        val response = MoveCommand(testDungeon, player, newPosition).invoke()
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
            it.terrain = Wall()
        }
        val response = MoveCommand(testDungeon, player, newPosition).invoke()
        Assertions.assertThat(response).isEqualTo(Pass)
        Assertions.assertThat(player.position).isEqualTo(startingPosition)
    }

    @Test
    fun `Moving to an occupied space should attack`() {
        val player: Player = testDungeon.player
        val monster: Monster = Monster()
        val monsterPos = Position3D.create(5, 5, 0)
        val initialMonsterHp = monster.health.hp
        testDungeon.addActor(monster, monsterPos)
        val response = MoveCommand(testDungeon, player, monsterPos).invoke()
        Assertions.assertThat(response).isEqualTo(Consumed)
        Assertions.assertThat(monster.health.hp).isLessThan(initialMonsterHp)
    }


}