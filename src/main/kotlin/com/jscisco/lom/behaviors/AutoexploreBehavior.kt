package com.jscisco.lom.behaviors

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord

class AutoexploreBehavior : BaseBehavior<GameContext>(AutoexploreAttribute::class, ActiveTurn::class) {

    var autoexploreCosts = Array<DoubleArray>(size = 0, init = {
        doubleArrayOf(0.0)
    })

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        if (entity.hasAttribute<AutoexploreAttribute>() && entity.hasAttribute<ActiveTurn>()) {
            calculateAutoexploreMap(entity, context.dungeon)
            val dijkstraMap: DijkstraMap = DijkstraMap(autoexploreCosts, DijkstraMap.Measurement.CHEBYSHEV)
            val stairsUp = context.dungeon.findPositionOfStairsUp(entity.position.z).get()
            val path = dijkstraMap.findPath(5, mutableListOf<Coord>(), mutableListOf<Coord>(), Coord.get(entity.position.x, entity.position.y),
                    Coord.get(stairsUp.x, stairsUp.y))
            if (path.size > 0) {
                entity.executeCommand(MoveCommand(context, entity, Position3D.create(path[0].x, path[0].y, entity.position.z)))
            } else {
                entity.removeAttribute(entity.attribute<AutoexploreAttribute>())
            }
        }
        return true
    }

    private fun calculateAutoexploreMap(entity: GameEntity<EntityType>, dungeon: Dungeon) {
        autoexploreCosts = Array(dungeon.actualSize().xLength, { DoubleArray(dungeon.actualSize().yLength) })
        for (x in 0 until dungeon.actualSize().xLength) {
            for (y in 0 until dungeon.actualSize().yLength) {
                val block = dungeon.fetchBlockOrDefault(Position3D.create(x, y, dungeon.player.position.z))
                if (block.seen.not()) {
                    autoexploreCosts[x][y] = 0.0
                } else {
                    autoexploreCosts[x][y] = DijkstraMap.FLOOR
                }
                if (block.isWall) {
                    autoexploreCosts[x][y] = DijkstraMap.WALL
                }
            }
        }
    }
}