package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.events.*
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.events.internal.ApplicationScope
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon

object DungeonEvents {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun registerEvents() {
        // Movement Events
        registerMovementEvent()

        // Combat Events
        registerInstigateCombatEvent()
        registerOnHitEvent()
        registerDamageEvent()
    }

    private fun registerMovementEvent() {
        Zircon.eventBus.subscribe<MoveEntityEvent> { (context, entity, position) ->
            logger.info("%s moved to %s.".format(entity, position))
            context.dungeon.moveEntity(entity, position)
            context.dungeon.updateCamera()
        }
    }

    private fun registerDamageEvent() {
        Zircon.eventBus.subscribe<DamageEvent> { (context, source, target, amount) ->
            // target.hp -= amount
            logger.info("%s dealt %s damage to %s".format(source, amount, target))
            Zircon.eventBus.publish(GameLogEvent("%s damage dealt to %s by %s".format(amount, target, source)))
            target.health.hpProperty.value -= amount
            logger.info("%s has %s health remaining.".format(target, target.health.hp))
            target.health.whenShouldBeDestroyed {
                Zircon.eventBus.publish(GameLogEvent("%s should be destroyed....".format(target)))
                context.dungeon.removeEntity(target)
            }
        }
    }

    private fun registerOnHitEvent() {
        Zircon.eventBus.subscribe<OnHitEvent> { (context, source, target) ->
            logger.info("%s hit %s!".format(source, target))
            Zircon.eventBus.publish(DamageEvent(context, source, target, 10))
        }
    }

    private fun registerInstigateCombatEvent() {
        Zircon.eventBus.subscribe<InstigateCombatEvent> { (context, source, target) ->
            logger.info("%s instigated combat against %s.".format(source, target))
            Zircon.eventBus.publish(OnHitEvent(context, source, target))
            Zircon.eventBus.publish(GameLogEvent("%s instigated combat against %s.".format(source, target)))
        }
    }
}