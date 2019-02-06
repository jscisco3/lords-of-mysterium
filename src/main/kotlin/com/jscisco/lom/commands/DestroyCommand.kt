package com.jscisco.lom.commands

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

data class DestroyCommand(override val context: GameContext,
                     override val source: GameEntity<EntityType>,
                     val cause: String) : GameCommand<EntityType>