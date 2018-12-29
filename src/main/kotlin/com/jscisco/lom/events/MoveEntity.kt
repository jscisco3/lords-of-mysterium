package com.jscisco.lom.events

import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.EntityType
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveEntity(val entity: GameEntity<EntityType>, val position: Position3D) : Event