package com.jscisco.lom.builders

import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Terrain
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.facets.Movable
import squidpony.squidmath.RNG

object EntityFactory {
    private val rng: RNG = RNG(0xDEADBEEF)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.PLAYER),
                Health(initalHp = 200)
        )
        facets(Movable)
    }

    fun newMonster() = newGameEntityOfType(NPC) {
        attributes(
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.GOBLIN),
                Health(initalHp = 25)
        )
    }

    fun newFloor() = newGameEntityOfType(Terrain) {
        attributes(EntityPosition(),
                EntityTile(GameTileBuilder.FLOOR)
        )
    }

    fun newWall() = newGameEntityOfType(Terrain) {
        attributes(BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.WALL)
        )
    }

}