package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute

class Equipment(val eligibleSlots: List<EquipmentSlot>, val initialEquipment: MutableMap<EquipmentSlot, GameEntity<Item>?>) : Attribute {

    private var slots: MutableMap<EquipmentSlot, GameEntity<Item>?> = initialEquipment

    fun getItemBySlot(slot: EquipmentSlot): GameEntity<Item>? {
        if (eligibleSlots.contains(slot).not()) {
            return null
        }
        return slots[slot]
    }

    fun equipItem(inventory: Inventory, slot: EquipmentSlot, newEquipment: GameEntity<Item>) {
        // If the equipment can't handle the slot we are trying to equip, do nothing
        if (eligibleSlots.contains(slot).not()) {
            return
        } else {
            unequipItem(inventory, slot)
            slots[slot] = newEquipment
        }
    }

    fun unequipItem(inventory: Inventory, slot: EquipmentSlot) {
        // If the equipment can't support the slot we are trying to unequip, do nothing
        if (eligibleSlots.contains(slot).not()) {
            return
        }
        val oldItem: GameEntity<Item>? = slots[slot]
        if (oldItem != null) {
            inventory.addItem(oldItem)
            slots[slot] = null
        }
    }
}