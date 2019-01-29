package com.jscisco.lom.entities

import com.jscisco.lom.attributes.types.FogOfWarType
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.fov
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.base.BaseEntity
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.graphics.Layer
import squidpony.squidgrid.FOV
import java.util.concurrent.ConcurrentHashMap

class FogOfWar(val dungeon: Dungeon, val player: GameEntity<Player>, val size: Size3D) : BaseEntity<FogOfWarType, GameContext>(FogOfWarType) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val fieldOfViewCalculator = FOV()

    private val fowPerLevel = ConcurrentHashMap<Int, Layer>().also { fows ->
        repeat(size.zLength) { level ->
            val fow = Layers.newBuilder()
                    .withSize(size.to2DSize())
                    .build()
                    .fill(GameTileBuilder.UNREVEALED)
            fows[level] = fow
            dungeon.pushOverlayAt(fow, level)
        }
    }

    init {
        updateFOW()
    }

    private fun updateFOW() {
        player.fov.fov = fieldOfViewCalculator.calculateFOV(dungeon.resistanceMap, player.position.x, player.position.y, player.fov.radius)
        val fov = player.fov.fov
        val blocks = dungeon.fetchBlocksAtLevel(player.position.z)
        for (x in 0 until fov.size) {
            for (y in 0 until fov[x].size) {
                // If the tile is in FOV, then the overlay should be EMPTY
                // Else:
                //      If the tile is out of FOV, but it is seen, then it should be SEEN_OUT_OF_SIGHT
                //      If the tile is out of FOV & not seen, then it should be UNREVEALED:
                val dungeonBlock = dungeon.fetchBlockOrDefault(Position3D.create(x, y, player.position.z))
                if (fov[x][y] > 0) {
                    // An empty tile essentially removes the overlay here
                    // Is this even how I want to handle it?
                    dungeonBlock.seen = true
                    dungeonBlock.inFov = true
                    dungeonBlock.lastSeen = dungeonBlock.layers.last()
                    fowPerLevel[player.position.z]?.setTileAt(Position.create(x, y), GameTileBuilder.EMPTY)
                } else {
                    dungeonBlock.inFov = false
                    if (dungeonBlock.seen) {
                        fowPerLevel[player.position.z]?.setTileAt(Position.create(x, y), GameTileBuilder.SEEN_OUT_OF_SIGHT)
                    } else {
                        fowPerLevel[player.position.z]?.setTileAt(Position.create(x, y), GameTileBuilder.UNREVEALED)

                    }
                }
            }
        }
        player.fov.fov
    }

    override fun update(context: GameContext): Boolean {
        updateFOW()
        return true
    }
}