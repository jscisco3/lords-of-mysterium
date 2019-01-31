package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.CombatStats
import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.HealthAttribute
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import org.hexworks.amethyst.api.entity.EntityType

interface Combatant : EntityType

val GameEntity<Combatant>.combatStats: CombatStats
    get() = attribute()

val GameEntity<Combatant>.equipment: EquipmentAttribute
    get() = attribute()

val GameEntity<Combatant>.health: HealthAttribute
    get() = attribute()