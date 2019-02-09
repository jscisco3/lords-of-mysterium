package com.jscisco.lom.commands

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

class AutoexploreCommand(override val context: GameContext,
                         override val source: GameEntity<EntityType>) : GameCommand<EntityType> {
}