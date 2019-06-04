package com.jscisco.lom.commands

import GameEntity
import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext

data class AttackCommand(override val context: GameContext,
                         override val source: GameEntity<Combatant>,
                         override val target: GameEntity<Combatant>) : EntityAction<Combatant, Combatant>