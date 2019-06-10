package com.jscisco.lom.facets

import GameCommand
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.responseWhenCommandIs
import com.jscisco.lom.extensions.tryActionsOn
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent

object Movable : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<MoveCommand> { (context, source, position) ->
        var response: Response = Pass
        val dungeon: Dungeon = context.dungeon
        dungeon.fetchBlockAt(position).ifPresent {
            if (it.isOccupied) {
                response = source.tryActionsOn(context, it.occupier)
            } else {
                if (dungeon.moveEntity(source, position)) {
                    response = Consumed
                }
            }
        }
        response
    }
}