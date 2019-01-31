package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.*
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.Openable
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.entities.FogOfWar
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.*
import org.hexworks.zircon.api.data.impl.Size3D
import java.util.*

object EntityFactory {
    private val random = Random(0xDEADBEEF)

    fun newFogOfWar(dungeon: Dungeon, player: GameEntity<Player>, size: Size3D) = FogOfWar(dungeon, player, size)

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(Player,
                NameAttribute("The Greatest Thief in the Multiverse"),
                StatBlockAttribute.create(
                        strength = 10,
                        intelligence = 8,
                        constitution = 12,
                        agility = 5,
                        perception = 6
                ),
                HealthAttribute.create(100),
                BlockOccupier,
                Inventory(maxWeight = 100),
                Equipment(listOf(
                        Equipment.EquipmentSlot(Equipment.EquipmentType.HEAD),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.AMULET),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.HAND),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.HAND),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.BODY),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.RING),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.RING),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.BOOTS),
                        Equipment.EquipmentSlot(Equipment.EquipmentType.TOOL)
                )),
                EntityTile(GameTileBuilder.PLAYER),
                EntityPosition(),
                FieldOfView(),
                EntityActions(AttackCommand::class, ToggleDoorCommand::class))
        facets(ItemPickerSystem,
                ItemDropperSystem,
                CombatSystem,
                MoveSystem,
                OpenDoorSystem)

    }

    fun newGoblin() = newGameEntityOfType(NPC) {
        attributes(EntityTile(GameTileBuilder.GOBLIN),
                NameAttribute("Snuggle Muffin"),
                StatBlockAttribute.create(
                        strength = 10,
                        intelligence = 8,
                        constitution = 12,
                        agility = 5,
                        perception = 6
                ),
                HealthAttribute.create(20),
                BlockOccupier,
                EntityPosition())
        facets(CombatSystem,
                MoveSystem)
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
        facets(OpenDoorSystem)
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

    fun noItem() = newGameEntityOfType(NoItem) {
        attributes()
    }
}
