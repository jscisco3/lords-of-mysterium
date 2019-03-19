package com.jscisco.lom.commands

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.AnyGameEntity
import com.jscisco.lom.extensions.GameCommand
import org.hexworks.amethyst.api.entity.EntityType

class EndTurnCommand(override val context: GameContext,
                     override val source: AnyGameEntity) : GameCommand<EntityType>