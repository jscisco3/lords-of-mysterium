package com.jscisco.lom.builders;

import com.jscisco.lom.entities.Entity
import com.jscisco.lom.entities.GameEntity
import com.jscisco.lom.entities.attributes.EntityMetadata
import com.jscisco.lom.entities.attributes.flags.Floor
import com.jscisco.lom.entities.attributes.flags.Player
import com.jscisco.lom.entities.attributes.flags.Wall
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newPlayer(): Entity {
        return GameEntity(
                metadata = EntityMetadata(
                        name = "Player",
                        tile = GameTileBuilder.PLAYER
                ),
                attributes = setOf(
                        Player
                )
        )
    }

    fun newWall(): Entity {
        return GameEntity(
                metadata = EntityMetadata(
                        name = "Wall",
                        tile = GameTileBuilder.wall()
                ),
                attributes = setOf(Wall)
        )
    }

    fun newFloor(): Entity {
        return GameEntity(
                metadata = EntityMetadata(
                        name = "Floor",
                        tile = GameTileBuilder.floor()
                ),
                attributes = setOf(Floor)
        )
    }
}
