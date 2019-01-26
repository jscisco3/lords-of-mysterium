package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.*
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Sword
import com.jscisco.lom.attributes.types.Wall
import com.jscisco.lom.commands.Attack
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.Combat
import com.jscisco.lom.systems.FieldOfViewSystem
import com.jscisco.lom.systems.ItemDropper
import com.jscisco.lom.systems.ItemPicker
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(Player,
                Health.create(100),
                BlockOccupier,
                Inventory(maxWeight = 100),
                EntityTile(GameTileBuilder.PLAYER),
                EntityPosition(),
                FieldOfView(),
                EntityActions(Attack::class))
        facets(ItemPicker,
                ItemDropper,
                FieldOfViewSystem,
                Combat)

    }

    fun newGoblin() = newGameEntityOfType(NPC) {
        attributes(EntityTile(GameTileBuilder.GOBLIN),
                Health.create(20),
                BlockOccupier,
                EntityPosition())
        facets(Combat)
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(BlockOccupier,
                VisionBlocker,
                EntityPosition(),
                EntityTile(GameTileBuilder.wall())
        )
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(
                CombatStats(
                        power = 5,
                        toHit = 5),
                ItemStats(
                        weight = 1,
                        cost = 10
                ),
                EntityTile(GameTileBuilder.sword()),
                EntityPosition())
    }
}
