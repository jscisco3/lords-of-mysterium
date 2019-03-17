package com.jscisco.lom.behaviors

import com.jscisco.lom.attributes.InitiativeAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.state.PlayerTurnState
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.initiative
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

class InitiativeBehavior : BaseBehavior<GameContext>(InitiativeAttribute::class) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * Return TRUE if we are recharging for the turn. This is because Behavior.OR will short circuit if the first
     * returns TRUE.
     */
    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        // Tick down
        entity.initiative.initiativeProperty.value -= 1
        logger.info("%s is %s ticks away from taking their turn.".format(entity.entityName, entity.initiative.initiative))
        // Once you hit 0, take a turn
        if (entity.initiative.initiative <= 0) {
            entity.addAttribute(ActiveTurn)
            if (entity.type == Player) {
                context.dungeon.pushState(PlayerTurnState())
                logger.info("%s now has an active turn.".format(entity.entityName))
            }
        }
        return true
    }
}