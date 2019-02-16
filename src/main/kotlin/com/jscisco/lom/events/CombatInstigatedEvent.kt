package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.systems.combat.Attack
import org.hexworks.cobalt.events.api.Event

data class CombatInstigatedEvent(val context: GameContext, val source: GameEntity<Combatant>, val target: GameEntity<Combatant>, val attack: Attack) : Event