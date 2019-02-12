package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.events.internal.ApplicationScope
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.builder.grid.TileGridBuilder
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.internal.Zircon
import org.junit.jupiter.api.Test

class TestCombatEvents {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val attacker: GameEntity<Combatant>
    val target: GameEntity<Combatant>
    val context: GameContext
    val dungeon: Dungeon

    init {
        // Clean up ApplicationScope
        Zircon.eventBus.cancelScope(ApplicationScope)
        attacker = EntityFactory.newPlayer()
        target = EntityFactory.newGoblin()


        val dungeonSize: Size3D = Size3D.create(100, 60, 2)
        val visibleSize: Size3D = Size3D.create(25, 25, 1)

        dungeon = DungeonBuilder(
                dungeonSize = dungeonSize,
                strategy = EmptyDungeonStrategy(dungeonSize),
                player = attacker
        ).build(visibleSize, dungeonSize)

        context = GameContext(
                dungeon = dungeon,
                screen = Screens.createScreenFor(TileGridBuilder.newBuilder().build()),
                input = KeyStroke('a'),
                player = attacker
        )
    }

    @Test
    fun testOnHitEvent() {
        Zircon.eventBus.publish(OnHitEvent(context, attacker, target))
    }

    @Test
    fun testInstigateCombatEvent() {
    }

    @Test
    fun testDamageEvent() {
        Zircon.eventBus.publish(DamageEvent(context, attacker, target, 50))
    }
}