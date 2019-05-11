package com.jscisco.lom.dungeon.state

import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.PopStateEvent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon
import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord

/**
 * We should utilize the AutoexploreBehavior to move automatically.
 */
class AutoexploreState(dungeon: Dungeon) : State(dungeon) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update() {
        val costMap: Array<DoubleArray> = calculateAutoexploreCosts()
        val dijkstraMap: DijkstraMap = DijkstraMap()
        dijkstraMap.initialize(costMap)
        val goals: Array<Coord> = getCoordsOfUnseenBlocks(dungeon.player.position.z)
        val playerCoord = Coord.get(dungeon.player.position.x, dungeon.player.position.y)
        val path = dijkstraMap.findPath(1, getCoordsOfEnemies(), listOf<Coord>(),
                playerCoord,
                *goals)
        if (path.isEmpty().not() && path[0] != playerCoord) {
            MoveCommand(dungeon, dungeon.player, Position3D.create(path[0].x, path[0].y, dungeon.player.position.z)).invoke()
        } else {
            Zircon.eventBus.publish(PopStateEvent())
        }
    }

    override fun handleInput(input: UIEvent): Response {
        if (input.type == KeyboardEventType.KEY_PRESSED) {
            val event = input as KeyboardEvent
            if (event.code == KeyCode.ESCAPE) {
                Zircon.eventBus.publish(PopStateEvent())
            }
        }
        return Pass
    }

    private fun calculateAutoexploreCosts(): Array<DoubleArray> {
        val autoExploreCosts = Array(dungeon.actualSize().xLength, { DoubleArray(dungeon.actualSize().yLength) })
        for (x in 0 until dungeon.actualSize().xLength) {
            for (y in 0 until dungeon.actualSize().yLength) {
                val block = dungeon.fetchBlockOrDefault(Position3D.create(x, y, dungeon.player.position.z))
                autoExploreCosts[x][y] = DijkstraMap.FLOOR
                if (block.isWalkable.not()) {
                    autoExploreCosts[x][y] = DijkstraMap.WALL
                }
            }
        }
        return autoExploreCosts
    }

    private fun getCoordsOfUnseenBlocks(level: Int): Array<Coord> {
        val goals = mutableListOf<Coord>()
        dungeon.fetchBlocksAtLevel(level).forEach {
            if (it.block.seen.not() && it.block.isOccupied.not()) {
                goals.add(Coord.get(it.position.x, it.position.y))
            }
        }
        return goals.toTypedArray()
    }

    private fun getCoordsOfEnemies(): List<Coord> {
        val enemyCoords = mutableListOf<Coord>()
        dungeon.monsters.forEach {
            enemyCoords.add(Coord.get(it.position.x, it.position.y))
        }
        return enemyCoords.toList()
    }

}