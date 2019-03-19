package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.commands.OpenDoorCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.DoorOpenedEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon

object OpenDoorSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<OpenDoorCommand> { (context, source, door) ->
        door.removeAttribute(VisionBlocker)
        door.removeAttribute(BlockOccupier)
        door.removeAttribute(door.attribute<EntityTile>())
        door.addAttribute(EntityTile(GameTileBuilder.openDoor()))
        logger.debug("Opening door")
        Zircon.eventBus.publish(DoorOpenedEvent(context, source, door))
        Consumed
    }
}