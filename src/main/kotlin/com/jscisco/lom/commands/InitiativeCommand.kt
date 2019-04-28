package com.jscisco.lom.commands

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

data class InitiativeCommand(override val context: GameContext,
                             override val source: GameEntity<EntityType>,
                             val decrement: Int) : GameCommand<EntityType> {
}