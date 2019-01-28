package com.jscisco.lom.systems

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object ToggleDoor : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<ToggleDoorCommand> { (context, source, target) ->
        context.dungeon.removeEntity(target)
        context.dungeon.addEntity(EntityFactory.newOpenDoor(), target.position)
        context.dungeon.calculateResistanceMap(context.dungeon.resistanceMap)
        Consumed
    }
}