package com.jscisco.lom.systems

import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.commands.InitiativeCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.state.AutoexploreState
import com.jscisco.lom.dungeon.state.PlayerTurnState
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.initiative
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

object InitiativeSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)


    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<InitiativeCommand> { (context, source, decrement) ->
        logger.info("%s has %s initiative and is reducing it by %s.".format(source.entityName, source.initiative.initiativeProperty.value, decrement))
        source.initiative.initiativeProperty.value -= decrement
        logger.info("%s has %s initiative.".format(source.entityName, source.initiative.initiativeProperty.value))
        if (source.initiative.initiative <= 0) {
            source.addAttribute(ActiveTurn)
            if (source.type == Player) {
                // If we aren't autoexploring, go to PlayerTurnState
                if (context.dungeon.currentState.javaClass != AutoexploreState::class.java) {
                    context.dungeon.pushState(PlayerTurnState())
                }
            }
            logger.info("%s now has an active turn.".format(source.entityName))
        }

        Consumed
    }
}