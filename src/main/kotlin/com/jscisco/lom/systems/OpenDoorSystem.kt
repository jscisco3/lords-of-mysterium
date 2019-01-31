package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.DoorOpenedEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.internal.Zircon

object OpenDoorSystem : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<ToggleDoorCommand> { (context, source, door) ->
        door.removeAttribute(VisionBlocker)
        door.removeAttribute(BlockOccupier)
        door.attribute<EntityTile>().tile = GameTileBuilder.openDoor()
        Zircon.eventBus.publish(DoorOpenedEvent(context, source, door))
        Consumed
    }
}