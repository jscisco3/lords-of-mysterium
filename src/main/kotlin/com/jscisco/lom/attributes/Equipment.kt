package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute

data class Equipment(val slot: Map<EquipmentSlot, GameEntity<Item>>) : Attribute