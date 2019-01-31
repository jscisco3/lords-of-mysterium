package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.EquippableAttribute
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.newGameEntityOfType
import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

interface Equipment : Item

val GameEntity<Equipment>.equippable: EquippableAttribute
    get() = attribute()

data class EquipmentSlot(var type: EquipmentType, val equippedItemProperty: Property<GameEntity<Equipment>>) {
    var disabled: Boolean = false
    var equippedItem: GameEntity<Equipment> by equippedItemProperty.asDelegate()

    companion object {
        fun create(type: EquipmentType, equippedItem: GameEntity<Equipment> = newGameEntityOfType(NoEquipment) { attributes() }) = EquipmentSlot(
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