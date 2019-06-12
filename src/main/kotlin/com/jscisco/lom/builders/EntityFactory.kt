package com.jscisco.lom.builders

import com.jscisco.lom.attributes.*
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Terrain
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.facets.Attackable
import com.jscisco.lom.facets.Movable
import squidpony.squidmath.RNG

object EntityFactory {
    private val rng: RNG = RNG(0xDEADBEEF)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.PLAYER),
                EntityActions(AttackCommand::class),
                Health(initalHp = 200),
                Initiative(cooldown = 1000)
        )
        facets(Movable,
                Attackable)
    }

    fun newMonster() = newGameEntityOfType(NPC) {
        attributes(
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.GOBLIN),
                Health(initalHp = 25),
                Initiative(cooldown = 1000)
        )
        facets(Movable,
                Attackable)
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