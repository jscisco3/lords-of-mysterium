package com.jscisco.lom.events

import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.events.api.Event

data class DamageEvent(val target: GameEntity<EntityType>, val source: GameEntity<EntityType>, val amount: Int) : Event