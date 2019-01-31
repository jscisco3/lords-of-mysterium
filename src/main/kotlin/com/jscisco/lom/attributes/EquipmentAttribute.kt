package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.EquipmentType
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.NoItem
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute

/**
 * eligibleSlots: The list of [EquipmentType] that an [Entity] can equip
 * initialEquipment: The list of [EquipmentSlot] that are already equipped.
 */
class EquipmentAttribute(val equipmentSlots: List<EquipmentSlot>) : Attribute {


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
        if (oldItem.type == NoItem) {
            slot.equippedItem = item
        } else {
            unequip(inventory, slot)
            slot.equippedItem = item
        }
    }

    fun unequip(inventory: Inventory, equipmentSlot: EquipmentSlot) {
        val oldItem: GameEntity<Item> = equipmentSlot.equippedItem
        if (oldItem.type != NoItem) {
            inventory.addItem(oldItem)
            equipmentSlot.equippedItem = EntityFactory.noItem()
        }
    }
}