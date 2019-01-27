package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.Toggleable
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity

data class ToggleDoorCommand(override val context: GameContext,
                             override val source: GameEntity<Toggleable>) : GameCommand<Toggleable>