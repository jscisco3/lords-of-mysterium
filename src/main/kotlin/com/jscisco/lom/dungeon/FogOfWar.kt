package com.jscisco.lom.dungeon

import com.jscisco.lom.builders.GameTileBuilder
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.Layer
import java.util.concurrent.ConcurrentHashMap

class FogOfWar(val dungeon: Dungeon) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val fowPerLevel = ConcurrentHashMap<Int, Layer>().also { fows ->
        repeat(dungeon.actualSize().zLength) { level ->
            val fow = Layers.newBuilder()
                    .withSize(dungeon.actualSize().to2DSize())
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
        dungeon.fetchBlocksAtLevel(dungeon.player.position.z).forEach {
            if (it.block.inFov) {
                fowPerLevel[it.position.z]?.setTileAt(Position.create(it.position.x, it.position.y), GameTileBuilder.EMPTY)
            } else {
                if (it.block.seen) {
                    fowPerLevel[it.position.z]?.setTileAt(Position.create(it.position.x, it.position.y), GameTileBuilder.SEEN_OUT_OF_SIGHT)
                } else {
                    fowPerLevel[it.position.z]?.setTileAt(Position.create(it.position.x, it.position.y), GameTileBuilder.UNREVEALED)
                }
            }
        }
    }
}