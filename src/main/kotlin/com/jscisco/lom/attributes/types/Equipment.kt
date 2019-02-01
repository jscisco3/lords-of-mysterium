package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.EquippableAttribute
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.newGameEntityOfType
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

val GameEntity<Item>.equippable: EquippableAttribute
    get() = attribute()

data class EquipmentSlot(var type: EquipmentType, val equippedItemProperty: Property<GameEntity<Item>>) {
    var disabled: Boolean = false
    var equippedItem: GameEntity<Item> by equippedItemProperty.asDelegate()

    companion object {
        fun create(type: EquipmentType, equippedItem: GameEntity<Item> = EntityFactory.noEquipment()) = EquipmentSlot(
                type = type,
                equippedItemProperty = createPropertyFrom(equippedItem)
        )
    }
}

enum class EquipmentType {
    HEAD,
    AMULET,
    HAND,
    BODY,
    RING,
    BOOTS,
    TOOL,
    TWO_HAND
}