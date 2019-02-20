package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.fov
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.whenHasAttribute
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.graphics.Layer
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius
import java.util.concurrent.ConcurrentHashMap

class FogOfWar(val dungeon: Dungeon, val player: GameEntity<Player>, val size: Size3D) {

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

    fun updateFOW() {
        player.whenHasAttribute<FieldOfView> {
            player.fov.fov = fieldOfViewCalculator.calculateFOV(dungeon.resistanceMap.getValue(player.position.z), player.position.x, player.position.y, player.fov.radius, Radius.DIAMOND)
            val fov = player.fov.fov
            for (x in 0 until fov.size) {
                for (y in 0 until fov[x].size) {
                    // If the tile is in FOV, then the overlay should be EMPTY
                    // Else:
                    //      If the tile is out of FOV, but it is seen, then it should be SEEN_OUT_OF_SIGHT
                    //      If the tile is out of FOV & not seen, then it should be UNREVEALED:
                    updateGameBlockAtPosition(Position3D.create(x, y, player.position.z), fov[x][y])
                }
            }
        }
    }

    private fun updateGameBlockAtPosition(position: Position3D, fovValue: Double) {
        val block = dungeon.fetchBlockOrDefault(position)
        if (fovValue > 0) {
            block.seen = true
            block.inFov = true
            block.lastSeen = block.layers.last()
            // Remove the overlay if it is in FOV
            fowPerLevel[position.z]?.setTileAt(Position.create(position.x, position.y), GameTileBuilder.EMPTY)
        } else {
            // This is not in FOV
            block.inFov = false
            if (block.seen) {
                // Not in FOV but we have seen it
                fowPerLevel[position.z]?.setTileAt(Position.create(position.x, position.y), GameTileBuilder.SEEN_OUT_OF_SIGHT)
            } else {
                // Never seen
                fowPerLevel[position.z]?.setTileAt(Position.create(position.x, position.y), GameTileBuilder.UNREVEALED)

            }
        }
    }
}