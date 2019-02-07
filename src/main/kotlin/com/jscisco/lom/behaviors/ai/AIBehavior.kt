package com.jscisco.lom.behaviors.ai

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.energy
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.system.Behavior
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

class AIBehavior(private val behaviors: Behavior<GameContext>) : BaseBehavior<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        while (entity.energy.energy > 0) {
            behaviors.update(entity, context)
        }
        return true
    }
}