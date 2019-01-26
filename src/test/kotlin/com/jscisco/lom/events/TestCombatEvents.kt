package com.jscisco.lom.events

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.EventRegistration
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCombatEvents {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val attacker: GameEntity<EntityType> = EntityFactory.newPlayer()
    val target: GameEntity<EntityType> = EntityFactory.newGoblin()

    @BeforeAll
    internal fun registerEvents() {
        EventRegistration.registerEvents()
    }

    @Test
    fun testOnHitEvent() {
        Zircon.eventBus.publish(OnHitEvent(target, attacker))
    }

    @Test
    fun testInstigateCombatEvent() {
    }

    @Test
    fun testDamageEvent() {
        Zircon.eventBus.publish(DamageEvent(target, attacker, 50))
    }
}