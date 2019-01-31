package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentType
import org.hexworks.amethyst.api.Attribute

data class EquippableAttribute(val equipmentType: EquipmentType) : Attribute