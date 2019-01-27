package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.openable
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import com.jscisco.lom.extensions.tile
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object ToggleDoor : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<ToggleDoorCommand> { (context, door) ->
        door.openable.toggle()
        if (door.openable.open) {
            door.tile
        }
        Consumed
    }
}