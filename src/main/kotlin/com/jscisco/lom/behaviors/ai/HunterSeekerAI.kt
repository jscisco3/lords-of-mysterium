package com.jscisco.lom.behaviors.ai

import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import squidpony.squidmath.AStarSearch

class HunterSeekerAI : BaseBehavior<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private var aStarMap = Array(1, { DoubleArray(1) })
    private var target: Position3D = Position3D.unknown()

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        // Assume we have a target
        target = updateTarget(context)
        if (target != Position3D.unknown()) {
            aStarMap = updateAStarMap(entity, context.dungeon)
            aStarMap[target.x][target.y] = 0.0
            entity.executeCommand(MoveCommand(context,
                    entity,
                    getNextCoord(entity, aStarMap)))
            return true
        }
        return false
    }

    private fun updateAStarMap(entity: GameEntity<EntityType>, dungeon: Dungeon): Array<DoubleArray> {
        aStarMap = Array(dungeon.actualSize().xLength, { DoubleArray(dungeon.actualSize().yLength) }).apply {
            dungeon.fetchBlocksAtLevel(entity.position.z).forEach {
                this[it.position.x][it.position.y] = if (it.block.isOccupied) 1.0 else 0.0
            }
        }
        return aStarMap
    }

    private fun getNextCoord(entity: GameEntity<EntityType>, aStartMap: Array<DoubleArray>): Position3D {
        val coord = AStarSearch(aStartMap, AStarSearch.SearchType.CHEBYSHEV).path(
                entity.position.x,
                entity.position.y,
                target.x,
                target.y
        ).remove()
        return Position3D.create(coord.x, coord.y, entity.position.z)
    }

    private fun updateTarget(context: GameContext): Position3D {
        return context.player.position
    }
}