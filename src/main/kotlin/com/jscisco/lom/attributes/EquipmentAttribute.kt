package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.EquipmentType
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.EntityFactory.NO_EQUIPMENT
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.entityName
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

/**
 * eligibleSlots: The list of [EquipmentType] that an [Entity] can equip
 * initialEquipment: The list of [EquipmentSlot] that are already equipped.
 */
class EquipmentAttribute(val equipmentSlots: List<EquipmentSlot>) : Attribute {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getItemsByType(type: EquipmentType): List<GameEntity<Item>> {
        return equipmentSlots.filter {
            it.type == type
        }.map {
            it.equippedItem
        }
    }

    fun getSlotsByType(type: EquipmentType): List<EquipmentSlot> {
        return equipmentSlots.filter { it.type == type }
    }

    fun equipItemToSlot(inventory: Inventory, slot: EquipmentSlot, item: GameEntity<Item>) {
        val oldItem = slot.equippedItem
        if (oldItem.entityName == NO_EQUIPMENT.entityName) {
            slot.equippedItem = item
        } else {
            unequip(inventory, slot)
            slot.equippedItemProperty.value = item
        }
    }

    fun unequip(inventory: Inventory, equipmentSlot: EquipmentSlot) {
        val oldItem: GameEntity<Item> = equipmentSlot.equippedItem
        if (oldItem.entityName != NO_EQUIPMENT.entityName) {
            inventory.addItem(oldItem)
            equipmentSlot.equippedItem = EntityFactory.NO_EQUIPMENT
        }
    }
}