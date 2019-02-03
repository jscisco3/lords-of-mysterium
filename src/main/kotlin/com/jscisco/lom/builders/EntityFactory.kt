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
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.systems.*
import com.jscisco.lom.trigger.Trigger
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.internal.Zircon
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
                Inventory(maxWeight = 100).also {
                    it.addItem(newSword())
                    it.addItem(newHelmet())
                },
                EquipmentAttribute(listOf(
                        EquipmentSlot.create(EquipmentType.HEAD),
                        EquipmentSlot.create(EquipmentType.AMULET),
                        EquipmentSlot.create(EquipmentType.HAND, newSword()),
                        EquipmentSlot.create(EquipmentType.HAND, newSword()),
                        EquipmentSlot.create(EquipmentType.BODY),
                        EquipmentSlot.create(EquipmentType.RING),
                        EquipmentSlot.create(EquipmentType.RING),
                        EquipmentSlot.create(EquipmentType.BOOTS),
                        EquipmentSlot.create(EquipmentType.TOOL)
                )),
                EntityTile(GameTileBuilder.PLAYER),
                EntityPosition(),
                FieldOfView(),
                EntityActions(AttackCommand::class, ToggleDoorCommand::class))
        facets(ItemPickerSystem,
                ItemDropperSystem,
                CombatSystem,
                MoveSystem,
                OpenDoorSystem,
                EquipItemSystem,
                UnequipItemSystem)

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
                HealthAttribute.create(50),
                BlockOccupier,
                EntityPosition())
        facets(CombatSystem,
                MoveSystem,
                DestroySystem)
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(BlockOccupier,
                VisionBlocker,
                EntityPosition(),
                EntityTile(GameTileBuilder.wall())
        )
    }

    fun newClosedDoor() = newGameEntityOfType(Door) {
        attributes(VisionBlocker,
                BlockOccupier,
                EntityPosition(),
                EntityTile(GameTileBuilder.closedDoor()),
                Openable)
        facets(OpenDoorSystem)
    }

    fun newSword() = newGameEntityOfType(Item) {
        attributes(
                NameAttribute(
                        name = "A Cool Sword"
                ),
                CombatStatsAttribute.create(
                        power = 5,
                        toHit = 5),
                ItemStats(
                        weight = 1,
                        cost = 10
                ),
                EquippableAttribute(EquipmentType.HAND),
                EntityTile(GameTileBuilder.sword()),
                EntityPosition())
    }

    fun newHelmet() = newGameEntityOfType(Item) {
        attributes(
                NameAttribute("A Helmet"),
                EquippableAttribute(EquipmentType.HEAD),
                EntityTile(GameTileBuilder.sword()),
                EntityPosition(),
                StatBlockAttribute.create(
                        constitution = 5
                ),
                TriggersAttribute(
                        mutableListOf(Trigger {
                            Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
                                println("A helmet heard $text!")
                            }
                        }.also { it.deactivate() })
                )
        )
    }

    fun noItem() = newGameEntityOfType(Item) {
        attributes(
                NameAttribute("No Item")
        )
    }

    fun noEquipment() = newGameEntityOfType(Item) {
        attributes(
                NameAttribute("No Equipment")
        )
    }

    val NO_ITEM = newGameEntityOfType(Item) {
        attributes(
                NameAttribute("No Item")
        )
    }

    val NO_EQUIPMENT = newGameEntityOfType(Item) {
        attributes(
                NameAttribute("No Equipment")
        )
    }
}
