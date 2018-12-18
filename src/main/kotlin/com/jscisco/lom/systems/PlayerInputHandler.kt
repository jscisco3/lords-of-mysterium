package com.jscisco.lom.systems

import com.jscisco.lom.dungeon.Context
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.kotlin.whenKeyStroke

object PlayerInputHandler : BaseSystem() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(context: Context) {
        val (dungeon, _, input, entity) = context
        if (true) {
            val player = entity
            logger.info("Updating player position from new input")
            val currentPosition = dungeon.findPositionOf(player).get()
            input.whenKeyStroke { ks ->
                val (newPos, direction) = when (ks.getCharacter()) {
                    'w' -> currentPosition.withRelativeY(-1) to "UP"
                    's' -> currentPosition.withRelativeY(1) to "DOWN"
                    'a' -> currentPosition.withRelativeX(-1) to "LEFT"
                    'd' -> currentPosition.withRelativeX(1) to "RIGHT"
                    else -> {
                        currentPosition to null
                    }
                }
            }
        }
    }
}