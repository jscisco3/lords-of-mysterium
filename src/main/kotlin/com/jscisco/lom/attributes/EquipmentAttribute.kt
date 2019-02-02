package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.EquipmentType
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.extensions.GameEntity
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
}