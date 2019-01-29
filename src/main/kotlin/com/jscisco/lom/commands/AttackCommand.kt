package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity

data class AttackCommand(override val context: GameContext,
                         override val source: GameEntity<Combatant>,
                         override val target: GameEntity<Combatant>
) : EntityAction<Combatant, Combatant>