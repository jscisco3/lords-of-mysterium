package com.jscisco.lom.events

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveEntityEvent(val context: GameContext, val entity: GameEntity<EntityType>, val position: Position3D) : Event