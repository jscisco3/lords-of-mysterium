package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.events.*
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.isPlayer
import com.jscisco.lom.extensions.tryActionsOn
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon

object DungeonEvents {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun registerEvents() {
        // Movement Events
        registerMovementEvent()
        registerOpenDoorEvent()

        // CombatSystem Events
        registerInstigateCombatEvent()
        registerOnHitEvent()
        registerDamageEvent()
    }

    private fun registerMovementEvent() {
        Zircon.eventBus.subscribe<MoveEntityEvent> { (context, entity, position) ->
            logger.debug("%s is trying to move to %s.".format(entity, position))
            val toBlock = context.dungeon.fetchBlockAt(position).get()
            if (toBlock.isOccupied) {
                entity.tryActionsOn(context, toBlock.occupier)
            } else {
                context.dungeon.moveEntity(entity, position)
                if (entity.isPlayer) {
                    // Only update the camera if the player moves
                    context.dungeon.updateCamera()
                }
            }
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

    private fun registerOpenDoorEvent() {
        Zircon.eventBus.subscribe<OpenDoorEvent> { (context, source, door) ->
            logger.info("%s opened the %s".format(source, door))
            door.removeAttribute(VisionBlocker)
            door.removeAttribute(BlockOccupier)
            door.attribute<EntityTile>().tile = GameTileBuilder.openDoor()
            context.dungeon.calculateResistanceMap(context.dungeon.resistanceMap)
        }
    }
}