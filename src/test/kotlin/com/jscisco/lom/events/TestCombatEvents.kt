package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.EventRegistration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.builder.grid.TileGridBuilder
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.internal.Zircon
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCombatEvents {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val attacker: GameEntity<Combatant> = EntityFactory.newPlayer()
    val target: GameEntity<Combatant> = EntityFactory.newGoblin()

    val player: GameEntity<Player> = EntityFactory.newPlayer()

    val dungeonSize: Size3D = Size3D.create(100, 60, 1)
    val visibleSize: Size3D = Size3D.create(25, 25, 1)

    val dungeon = DungeonBuilder(
            dungeonSize = dungeonSize,
            strategy = EmptyDungeonStrategy(dungeonSize),
            player = player
    ).build(visibleSize, dungeonSize)

    val context = GameContext(
            dungeon = dungeon,
            screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
            input = KeyStroke('a'),
            player = player
    )

    @BeforeAll
    internal fun registerEvents() {
        EventRegistration.registerEvents()
    }

    @Test
    fun testOnHitEvent() {
        Zircon.eventBus.publish(OnHitEvent(context, target, attacker))
    }

    @Test
    fun testInstigateCombatEvent() {
    }

    @Test
    fun testDamageEvent() {
        Zircon.eventBus.publish(DamageEvent(context, target, attacker, 50))
    }
}