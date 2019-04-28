package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.InitiativeAttribute
import com.jscisco.lom.attributes.types.Generic
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.InitiativeSystem
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.junit.jupiter.api.Test

class TestInitativeCalculator {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val dungeon: Dungeon = mockk()


    @Test
    fun `Initiative decrement should be calculated correctly`() {
        val testEntity = entityWithInitiative()

        every {
            dungeon.fetchEntitiesOnZLevel(any())
        } returns listOf<GameEntity<Generic>>(testEntity)

        val decrement = InitiativeCalculator.calculateInitiativeDecrement(dungeon)
        Assertions.assertThat(decrement).isEqualTo(100)
    }

    private fun entityWithInitiative() = newGameEntityOfType(Generic) {
        attributes(InitiativeAttribute.create(initiative = 100))
        facets(InitiativeSystem)
    }

}