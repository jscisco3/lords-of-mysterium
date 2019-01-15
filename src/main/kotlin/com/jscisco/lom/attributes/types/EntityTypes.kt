package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.CombatStats
import com.jscisco.lom.attributes.ItemStats
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import org.hexworks.amethyst.api.base.BaseEntityType

object Player : BaseEntityType(
        name = "player"
), ItemHolder

object Wall : BaseEntityType(
        name = "wall"
)

object Sword : BaseEntityType(
        name = "sword"
), Item

object NoItem : BaseEntityType(
        name = "Select an item"
), Item

val GameEntity<Item>.power: Int
    get() = attribute<CombatStats>().power

val GameEntity<Item>.toHit: Int
    get() = attribute<CombatStats>().toHit

val GameEntity<Item>.cost: Int
    get() = attribute<ItemStats>().cost

val GameEntity<Item>.weight: Int
    get() = attribute<ItemStats>().weight