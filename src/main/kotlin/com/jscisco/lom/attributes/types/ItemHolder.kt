package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import org.hexworks.amethyst.api.entity.EntityType

interface ItemHolder : EntityType

val GameEntity<ItemHolder>.inventory: Inventory
    get() = attribute()