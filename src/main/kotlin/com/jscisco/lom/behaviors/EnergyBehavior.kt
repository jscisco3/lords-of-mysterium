package com.jscisco.lom.behaviors

import com.jscisco.lom.attributes.EnergyAttribute
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.energy
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

class EnergyBehavior : BaseBehavior<GameContext>(EnergyAttribute::class) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * Return TRUE if we are recharging for the turn. This is because Behavior.OR will short circuit if the first
     * returns TRUE.
     */
    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        if (entity.energy.energy <= 0) {
            entity.energy.recharge()
            return true
        }
        return false
    }
}