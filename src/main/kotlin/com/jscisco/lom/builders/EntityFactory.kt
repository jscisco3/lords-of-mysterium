package com.jscisco.lom.builders;

import com.jscisco.lom.attributes.*
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.Openable
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.behaviors.InitiativeBehavior
import com.jscisco.lom.behaviors.ai.AIBehavior
import com.jscisco.lom.behaviors.ai.HunterSeekerAI
import com.jscisco.lom.behaviors.ai.WanderAI
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.OpenDoorCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.entities.FogOfWar
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.events.scope.EntityEventScope
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.extensions.triggers
import com.jscisco.lom.systems.*
import com.jscisco.lom.systems.combat.CombatSystem
import com.jscisco.lom.trigger.Trigger
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.data.impl.Position3D
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
                FieldOfView(
                        radius = 10.0
                ),
                EventListenerAttribute(EntityEventScope()),
                EntityActions(AttackCommand::class, OpenDoorCommand::class),
                InitiativeAttribute.create(
                        initiative = 0
                ))
        behaviors(InitiativeBehavior())
        facets(ItemPickerSystem,
                ItemDropperSystem,
                CombatSystem,
                MoveSystem,
                OpenDoorSystem,
                EquipItemSystem,
                UnequipItemSystem,
                StairAscender,
                StairDescender)

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
                EntityPosition(),
                EntityActions(AttackCommand::class),
                InitiativeAttribute.create(
                        initiative = 2
                ))
        behaviors(
                InitiativeBehavior(),
                AIBehavior(HunterSeekerAI(updateTarget = { Position3D.unknown() }).or(WanderAI()))
        )
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

    fun newOpenDoor() = newGameEntityOfType(Door) {
        attributes(EntityPosition(),
                EntityTile(GameTileBuilder.openDoor()))
    }

    fun newSword() = newGameEntityOfType(Item) {
        attributes(
                NameAttribute(
                        name = "A Cool Sword"
                ),
                StatBlockAttribute.create(
                        attackPower = 5,
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
                        mutableListOf()
                )
        )
    }.also {
        it.triggers.add(Trigger {
            Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
                println("A helmet heard $text!")
            }
        }.also { trigger ->
            trigger.deactivate()
        })
    }

    fun stairsUp() = newGameEntityOfType(StairsUp) {
        attributes(
                EntityTile(GameTileBuilder.STAIRS_UP)
        )

    }

    fun stairsDown() = newGameEntityOfType(StairsDown) {
        attributes(
                EntityTile(GameTileBuilder.STAIRS_DOWN)
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
