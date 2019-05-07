package com.jscisco.lom.systems

import com.jscisco.lom.commands.DescendStairsCommand
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.isPlayer
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent

object StairDescender : BaseFacet<GameContext>() {

    override fun executeCommand(command: Command<out EntityType, GameContext>): Response = command.responseWhenCommandIs<DescendStairsCommand> { (context, source) ->
        // Move the entity down a floor
        context.dungeon.fetchBlockAt(source.position).ifPresent {
            if (it.isStairsDown) {
                source.executeCommand(MoveCommand(context, source, source.position.withRelativeZ(1)))
                context.dungeon.findPositionOfStairsUp(source.position.z).ifPresent {
                    source.executeCommand(MoveCommand(context, source, it))
                }
                if (source.isPlayer) {
                    context.dungeon.scrollOneDown()
                }
            }
        }
        Consumed
    }

}