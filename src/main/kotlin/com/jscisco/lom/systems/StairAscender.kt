package com.jscisco.lom.systems

import com.jscisco.lom.commands.AscendStairsCommand
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.isPlayer
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent

object StairAscender : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AscendStairsCommand> { (context, source) ->
        context.dungeon.fetchBlockAt(source.position).ifPresent {
            if (it.isStairsUp) {
                source.executeCommand(MoveCommand(context, source, source.position.withRelativeZ(-1)))
                context.dungeon.findPositionOfStairsDown(source.position.z).ifPresent {
                    source.executeCommand(MoveCommand(context, source, it))
                }
                if (source.isPlayer) {
                    context.dungeon.scrollOneUp()
                }
            }
        }
        Consumed

    }

}