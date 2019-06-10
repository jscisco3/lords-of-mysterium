package com.jscisco.lom.commands

import GameEntity
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.data.TestData
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.amethyst.api.Consumed
import org.junit.jupiter.api.Test

class TestAttackCommand {

    private val testDungeon: Dungeon = TestData.newTestDungeon()
    private val context: GameContext = GameContext(dungeon = testDungeon,
            screen = mockk(),
            uiEvent = mockk())

    @Test
    fun `when attacked, hp should decrease`() {
        val player: GameEntity<Player> = testDungeon.player
        val monster: GameEntity<NPC> = EntityFactory.newMonster()
        val initialMonsterHp = monster.findAttribute(Health::class).get().hp
        val response = player.executeCommand(AttackCommand(context, player as GameEntity<Combatant>, monster as GameEntity<Combatant>))
        Assertions.assertThat(response).isEqualTo(Consumed)
        Assertions.assertThat(monster.findAttribute(Health::class).get().hp).isLessThan(initialMonsterHp)
    }

}