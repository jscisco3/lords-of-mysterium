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
        for (x in 0 until fov.size) {
            for (y in 0 until fov[x].size) {
                if (fov[x][y] > 0) {
                    fowPerLevel[player.position.z]?.setTileAt(Position.create(x, y), GameTileBuilder.EMPTY)
                } else {
                    fowPerLevel[player.position.z]?.setTileAt(Position.create(x, y), GameTileBuilder.UNREVEALED)
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