package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.CombatStats
import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.attributes.ItemStats
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Wall
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.ItemPicker
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(Player,
                BlockOccupier,
                Inventory(maxWeight = 100),
                EntityTile(GameTileBuilder.PLAYER))
        facets(ItemPicker)

    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(BlockOccupier,
                VisionBlocker,
                EntityTile(GameTileBuilder.wall())
        )
    }

    fun newSword() = newGameEntityOfType(Item) {
        attributes(
                CombatStats(
                        power = 5,
                        toHit = 5),
                ItemStats(
                        weight = 1,
                        cost = 10
                ),
                EntityTile(GameTileBuilder.sword()))
    }
}
