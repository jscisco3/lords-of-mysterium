package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.*
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.Openable
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.commands.Attack
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.*
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
                EntityActions(Attack::class, ToggleDoorCommand::class))
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

    fun newOpenDoor() = newGameEntityOfType(Door) {
        attributes(EntityPosition(),
                EntityTile(GameTileBuilder.openDoor()),
                Openable)
    }

    fun newClosedDoor() = newGameEntityOfType(Door) {
        attributes(VisionBlocker,
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.closedDoor()),
                Openable)
        facets(ToggleDoor)
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
