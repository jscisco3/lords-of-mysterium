package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import org.hexworks.amethyst.api.Attribute

data class Equippable(val validSlots: Array<EquipmentSlot>) : Attribute {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Equippable) return false

        if (!validSlots.contentEquals(other.validSlots)) return false

        return true
    }

    override fun hashCode(): Int {
        return validSlots.contentHashCode()
    }
}