package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.Equipment.EquipmentSlot

import org.hexworks.amethyst.api.Attribute

data class Equippable(val slot: EquipmentSlot) : Attribute