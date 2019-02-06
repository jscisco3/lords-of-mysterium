package com.jscisco.lom.behaviors.ai

import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

class WanderAI : BaseBehavior<GameContext>() {
    // NORTH, SOUTH, EAST, WEST
    // NORTHEAST, SOUTHEAST, NORTHWEST, SOUTHWEST
    val directions = listOf(
            Position3D.create(0, -1, 0),
            Position3D.create(0, 1, 0),
            Position3D.create(1, 0, 0),
            Position3D.create(-1, 0, 0),
            Position3D.create(1, -1, 0),
            Position3D.create(1, 1, 0),
            Position3D.create(-1, -1, 0),
            Position3D.create(-1, 1, 0))

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        entity.executeCommand(MoveCommand(context, entity, entity.position.withRelative(directions.random())))
        return true
    }
}