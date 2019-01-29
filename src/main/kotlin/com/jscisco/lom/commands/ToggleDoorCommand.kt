package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.Door
import com.jscisco.lom.attributes.types.Toggleable
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

data class ToggleDoorCommand(override val context: GameContext,
                             override val source: GameEntity<EntityType>,
                             override val target: GameEntity<Door>) : EntityAction<EntityType, Toggleable>