package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.Equipment.EquipmentSlot
import com.jscisco.lom.attributes.Equipment.EquipmentType
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.NoItem
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.internal.extensions.getIfPresent

/**
 * eligibleSlots: The list of [EquipmentType] that an [Entity] can equip
 * initialEquipment: The list of [EquipmentSlot] that are already equipped.
 */
class Equipment(val equipment: List<Equipment.EquipmentSlot>) : Attribute {


    fun getItemsByType(type: EquipmentType): List<GameEntity<Item>> {
        return equipment.filter {
            it.type == type
        }.map {
            it.equippedItem
        }
    }

    fun getSlotsByType(type: EquipmentType): List<EquipmentSlot> {
        return equipment.filter { it.type == type }
    }

    fun equipItem(inventory: Inventory, type: EquipmentType, item: GameEntity<Item>) {
        getSlotsByType(type).getIfPresent(0).map {
            val oldItem = it.equippedItem
            if (oldItem.type == NoItem) {
                it.equippedItem = item
            } else {
                unequip(inventory, it)
                it.equippedItem = item
            }
        }
    }

    fun unequip(inventory: Inventory, equipmentSlot: EquipmentSlot) {
        val oldItem: GameEntity<Item> = equipmentSlot.equippedItem
        if (oldItem.type != NoItem) {
            inventory.addItem(oldItem)
            equipmentSlot.equippedItem = EntityFactory.noItem()
        }
    }

    data class EquipmentSlot(var type: EquipmentType) {
        var disabled: Boolean = false
        var equippedItem: GameEntity<Item> = newGameEntityOfType(NoItem) { attributes() }
    }

    enum class EquipmentType {
        HAND,
        TWO_HAND,
        HEAD,
        BODY,
        RING,
        AMULET,
        TOOL
    }
}