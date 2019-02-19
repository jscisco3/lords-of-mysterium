package com.jscisco.lom.systems

import com.jscisco.lom.attributes.InitiativeAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.Generic
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.behaviors.InitiativeBehavior
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.initiative
import com.jscisco.lom.extensions.newGameEntityOfType
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.amethyst.api.Engines
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.junit.jupiter.api.Test

class TestInitiativeSystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `when my initiative has hit 0, I should be given a turn`() {
        val engine = Engines.newEngine<GameContext>()
        engine.addEntity(testEntity)
        Assertions.assertThat(testEntity.hasAttribute<ActiveTurn>()).isFalse()
        for (i in 0..testEntity.initiative.initiative) {
            engine.update(mockk())
        }
        Assertions.assertThat(testEntity.hasAttribute<ActiveTurn>()).isTrue()
    }

    @Test
    fun `when the player has a turn, we should pause other systems`() {
        val engine = Engines.newEngine<GameContext>()
    }

    private val testPlayer = newGameEntityOfType(Player) {
        attributes(InitiativeAttribute.create(
                initiative = 100
        ))
        behaviors(InitiativeBehavior())
    }

    private val testEntity = newGameEntityOfType(Generic) {
        attributes(InitiativeAttribute.create(
                initiative = 100
        ))
        behaviors(InitiativeBehavior())
    }


}