package com.jscisco.lom.systems

import com.jscisco.lom.attributes.InitiativeAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.Generic
import com.jscisco.lom.commands.InitiativeCommand
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.newGameEntityOfType
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.junit.jupiter.api.Test

class TestInitiativeSystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `when my initiative has hit 0, I should be given a turn`() {
        Assertions.assertThat(testEntity.hasAttribute<ActiveTurn>()).isFalse()

        testEntity.executeCommand(InitiativeCommand(
                mockk(),
                testEntity,
                100
        ))

        Assertions.assertThat(testEntity.hasAttribute<ActiveTurn>()).isTrue()
    }

    private val testEntity = newGameEntityOfType(Generic) {
        attributes(InitiativeAttribute.create(
                initiative = 100
        ))
        facets(InitiativeSystem)
    }
}