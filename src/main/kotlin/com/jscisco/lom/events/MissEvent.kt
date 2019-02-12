package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.events.api.Event

data class MissEvent(val context: GameContext,
                     val attacker: GameEntity<Combatant>,
                     val defender: GameEntity<Combatant>) : Event