package com.jscisco.lom.behaviors

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.commands.EndTurnCommand
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CancelAutoexplore
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.internal.Zircon
import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord

class AutoexploreBehavior : BaseBehavior<GameContext>() {

    private var logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        // This should only work when I have both the AutoexploreAttribute && an ActiveTurn
        if (entity.hasAttribute<AutoexploreAttribute>() && entity.hasAttribute<ActiveTurn>()) {
            val dijkstraMap = entity.attribute<AutoexploreAttribute>().dijkstraMap
            dijkstraMap.initialize(calculateAutoexploreMap(entity, context.dungeon))

            val goals = getCoordsOfUnseenBlocks(context.dungeon, entity.position.z)

            var path = dijkstraMap.findPath(1, getCoordsOfEnemies(context.dungeon), mutableListOf<Coord>(), Coord.get(entity.position.x, entity.position.y),
                    *goals)
            if (path.size == 0 && context.dungeon.findPositionOfStairsUp(entity.position.z).isPresent) {
                path = dijkstraMap.findPath(1, getCoordsOfEnemies(context.dungeon), mutableListOf<Coord>(), Coord.get(entity.position.x, entity.position.y),
                        getCoordsOfStairsUp(context.dungeon, entity.position.z))
            }
            if (goals.isEmpty()) {
                logger.info("No goals")
                Zircon.eventBus.publish(CancelAutoexplore(entity))
            }
            if (path.size > 0 && path[0] != Coord.get(entity.position.x, entity.position.y)) {
                entity.executeCommand(MoveCommand(context, entity, Position3D.create(path[0].x, path[0].y, entity.position.z))).also {
                    if (it == Consumed) {
                        entity.executeCommand(EndTurnCommand(context, entity))
                    }
                }
            } else {
                logger.info("I can't find a path")
                Zircon.eventBus.publish(CancelAutoexplore(entity))
            }
        }
        return true
    }

    fun calculateAutoexploreMap(entity: GameEntity<EntityType>, dungeon: Dungeon): Array<DoubleArray> {
        val autoexploreCosts = Array(dungeon.actualSize().xLength, { DoubleArray(dungeon.actualSize().yLength) })
        for (x in 0 until dungeon.actualSize().xLength) {
            for (y in 0 until dungeon.actualSize().yLength) {
                val block = dungeon.fetchBlockOrDefault(Position3D.create(x, y, dungeon.player.position.z))
                autoexploreCosts[x][y] = DijkstraMap.FLOOR
                if (block.isClosedDoor) {
                    autoexploreCosts[x][y] = DijkstraMap.FLOOR
                }
                if (block.isWall) {
                    autoexploreCosts[x][y] = DijkstraMap.WALL
                }
            }
        }
        return autoexploreCosts
    }

    private fun getCoordsOfEnemies(dungeon: Dungeon): MutableList<Coord> {
        val enemyCoords = mutableListOf<Coord>()
        dungeon.enemyList.forEach {
            enemyCoords.add(Coord.get(it.position.x, it.position.y))
        }
        return enemyCoords
    }

    private fun getCoordsOfUnseenBlocks(dungeon: Dungeon, level: Int): Array<Coord> {
        val goals = mutableListOf<Coord>()
        dungeon.fetchBlocksAtLevel(level).forEach {
            if (it.block.seen.not() && it.block.isOccupied.not()) {
                goals.add(Coord.get(it.position.x, it.position.y))
            }
        }
        return goals.toTypedArray()
    }

    private fun getCoordsOfStairsUp(dungeon: Dungeon, level: Int): Coord {
        val stairsUp = dungeon.findPositionOfStairsUp(level).get()
        return Coord.get(stairsUp.x, stairsUp.y)
    }

}