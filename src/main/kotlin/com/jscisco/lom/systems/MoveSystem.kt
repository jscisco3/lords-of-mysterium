package com.jscisco.lom.systems

import com.jscisco.lom.attributes.InitiativeAttribute
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.EntityMovedEvent
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon

object MoveSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<MoveCommand> { (context, source, position) ->
        logger.debug("%s is trying to move to %s.".format(source.entityName, position))
        context.dungeon.fetchBlockAt(position).ifPresent {
            if (it.isOccupied) {
                source.tryActionsOn(context, it.occupier)
            } else {
                if (context.dungeon.moveEntity(source, position)) {
                    source.whenHasAttribute<InitiativeAttribute> { initiative ->
                        initiative.initiativeProperty.value += 10
                    }
                    Zircon.eventBus.publish(EntityMovedEvent(source, position))
                }
            }
        }
        Consumed
    }
}