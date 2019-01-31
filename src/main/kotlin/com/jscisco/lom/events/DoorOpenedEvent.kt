package com.jscisco.lom.events

import com.jscisco.lom.attributes.types.Door
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.events.api.Event

data class DoorOpenedEvent(val context: GameContext,
                           val opener: GameEntity<EntityType>,
                           val door: GameEntity<Door>) : Event