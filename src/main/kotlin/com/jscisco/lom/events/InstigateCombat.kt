package com.jscisco.lom.events

import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.events.api.Event

data class InstigateCombat(val source: GameEntity<EntityType>, val target: GameEntity<EntityType>) : Event