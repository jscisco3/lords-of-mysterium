package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.CombatStats
import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.ItemStats
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import org.hexworks.amethyst.api.base.BaseEntityType
import org.hexworks.amethyst.api.entity.EntityType

object Player : BaseEntityType(
        name = "player"
), ItemHolder, Combatant

object NPC : BaseEntityType(
        name = "npc"
), ItemHolder, Combatant

object Wall : BaseEntityType(
        name = "wall"
)

object Item : BaseEntityType(
        name = "sword"
)

object Door : BaseEntityType(
        name = "Door"
), Toggleable

object ItemType : BaseEntityType(
        name = "Item"
)

object FogOfWarType : BaseEntityType()

val GameEntity<Item>.power: Int
    get() = attribute<CombatStats>().power

val GameEntity<Item>.toHit: Int
    get() = attribute<CombatStats>().toHit

val GameEntity<Item>.cost: Int
    get() = attribute<ItemStats>().cost

val GameEntity<Item>.weight: Int
    get() = attribute<ItemStats>().weight

val GameEntity<EntityType>.fov: FieldOfView
    get() = attribute()