package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.fov
import com.jscisco.lom.commands.FieldOfView
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import squidpony.squidgrid.FOV

object FieldOfViewSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<FieldOfView> { (context: GameContext, source) ->
        val fieldOfViewCalculator = FOV()
        source.fov.fov = fieldOfViewCalculator.calculateFOV(context.dungeon.resistanceMap, source.position.x, source.position.y, source.fov.radius)
        Consumed
    }
}