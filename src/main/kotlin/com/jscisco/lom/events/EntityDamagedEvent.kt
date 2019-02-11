package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.events.api.Event

data class EntityDamagedEvent(val context: GameContext, val sourceOfDamage: GameEntity<Combatant>, val receiverOfDamage: GameEntity<Combatant>, val amount: Int) : Event