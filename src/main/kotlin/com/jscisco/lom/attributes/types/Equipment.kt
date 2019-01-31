package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.EquippableAttribute
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.newGameEntityOfType

interface Equipment : Item

val GameEntity<Equipment>.equippable: EquippableAttribute
    get() = attribute()

data class EquipmentSlot(var type: EquipmentType) {
    var disabled: Boolean = false
    var equippedItem: GameEntity<Item> = newGameEntityOfType(NoItem) { attributes() }
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