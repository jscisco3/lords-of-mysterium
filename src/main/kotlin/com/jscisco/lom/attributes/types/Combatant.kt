package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.CombatStatsAttribute
import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.HealthAttribute
import com.jscisco.lom.attributes.StatBlockAttribute
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.statBlock
import com.jscisco.lom.extensions.whenHasAttribute
import org.hexworks.amethyst.api.entity.EntityType

interface Combatant : EntityType

val GameEntity<Combatant>.combatStats: CombatStatsAttribute
    get() = attribute()

val GameEntity<ItemHolder>.equipment: EquipmentAttribute
    get() = attribute()

val GameEntity<Combatant>.health: HealthAttribute
    get() = attribute()

val GameEntity<Combatant>.combinedStats: StatBlockAttribute
    get() {
        val baseStats = StatBlockAttribute.create()
        this.whenHasAttribute<StatBlockAttribute> {
            baseStats + it
        }
        this.whenHasAttribute<EquipmentAttribute> {
            it.equipmentSlots.forEach { eq ->
                eq.equippedItem.whenHasAttribute<StatBlockAttribute> { sba ->
                    baseStats + sba
                }
            }
        }
        return baseStats
    }

val GameEntity<Combatant>.toHit: Int
    get() = this.combinedStats.toHitProperty.value

val GameEntity<Combatant>.ev: Int
    get() = this.combinedStats.evProperty.value

val GameEntity<Combatant>.attackPower: Int
    get() = this.combinedStats.attackPowerProperty.value

val GameEntity<Combatant>.ac: Int
    get() = this.combinedStats.acProperty.value