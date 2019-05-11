package com.jscisco.lom.commands

import com.jscisco.lom.actor.Monster
import com.jscisco.lom.actor.Player
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestAttackCommand {

    @Test
    fun `when attacked, hp should decrease`() {
        val player: Player = Player()
        val monster: Monster = Monster()
        val startingMonsterHp: Int = monster.health.hp
        AttackCommand(mockk(), player, monster).invoke()
        Assertions.assertThat(monster.health.hp).isLessThan(startingMonsterHp)
    }

}