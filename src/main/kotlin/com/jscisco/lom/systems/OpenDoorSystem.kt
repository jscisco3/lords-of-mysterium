package com.jscisco.lom.systems

import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.OpenDoorEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.internal.Zircon

object OpenDoorSystem : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<ToggleDoorCommand> { (context, source, target) ->
        Zircon.eventBus.publish(OpenDoorEvent(context, source, target))
        Consumed
    }
}