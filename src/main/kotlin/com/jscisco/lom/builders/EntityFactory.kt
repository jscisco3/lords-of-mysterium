package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Wall
import com.jscisco.lom.extensions.newGameEntityOfType
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(Player,
                BlockOccupier,
                EntityTile(GameTileBuilder.PLAYER))

    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(BlockOccupier,
                VisionBlocker,
                EntityTile(GameTileBuilder.wall())
        )
    }
}
