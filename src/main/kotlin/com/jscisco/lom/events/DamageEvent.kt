package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.events.api.Event

data class DamageEvent(val context: GameContext, val source: GameEntity<Combatant>, val target: GameEntity<Combatant>, val amount: Int) : Event