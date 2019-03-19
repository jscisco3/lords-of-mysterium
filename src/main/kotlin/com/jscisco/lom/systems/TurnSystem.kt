package com.jscisco.lom.systems

import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.commands.EndTurnCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import com.jscisco.lom.extensions.whenHasAttribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object TurnSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<EndTurnCommand> {
        val source = it.source
        source.whenHasAttribute<ActiveTurn> {
            source.removeAttribute(ActiveTurn)
        }
        Consumed
    }
}