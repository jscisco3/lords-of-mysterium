package com.jscisco.lom.behaviors.ai

import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import squidpony.squidmath.AStarSearch

class HunterSeekerAI : BaseBehavior<GameContext>() {

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val aStarMap = Array(context.dungeon.actualSize().xLength, { DoubleArray(context.dungeon.actualSize().yLength) }).apply {
            context.dungeon.fetchBlocksAtLevel(entity.position.z).forEach {
                this[it.position.x][it.position.y] = if (it.block.isOccupied) 1.0 else 0.0
            }
        }
        aStarMap[context.dungeon.player.position.x][context.dungeon.player.position.y] = 0.0
        val search = AStarSearch(aStarMap, AStarSearch.SearchType.CHEBYSHEV)
        val coord = search.path(entity.position.x, entity.position.y, context.dungeon.player.position.x, context.dungeon.player.position.y).remove()
        entity.executeCommand(MoveCommand(context,
                entity,
                Position3D.create(coord.x, coord.y, entity.position.z)))
        return true
    }
}