package com.jscisco.lom.builders;

import com.jscisco.lom.entities.Entity
import com.jscisco.lom.entities.GameEntity
import com.jscisco.lom.entities.attributes.EntityMetadata
import com.jscisco.lom.entities.attributes.flags.Player
import com.jscisco.lom.entities.attributes.flags.Wall
import com.jscisco.lom.systems.PlayerInputHandler
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newPlayer(): Entity {
        return GameEntity(
                metadata = EntityMetadata(
                        name = "Player",
                        tile = GameTileRepository.PLAYER
                ),
                attributes = setOf(
                        Player
                ),
                systems = setOf(
                        PlayerInputHandler
                )
        )
    }

    fun newWall(): Entity {
        return GameEntity(
                metadata = EntityMetadata(
                        name = "Wall",
                        tile = GameTileRepository.wall()
                ),
                attributes = setOf(Wall)
        )
    }
}
