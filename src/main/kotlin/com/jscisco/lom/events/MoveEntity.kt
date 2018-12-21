package com.jscisco.lom.events

import com.jscisco.lom.entities.Entity
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveEntity(val entity: Entity, val position: Position3D) : Event