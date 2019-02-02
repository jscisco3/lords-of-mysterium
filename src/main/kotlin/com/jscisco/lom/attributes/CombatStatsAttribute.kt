package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

data class CombatStatsAttribute(val powerProperty: Property<Int>, val toHitProperty: Property<Int>) : Attribute {

    companion object {
        fun create(power: Int,
                   toHit: Int): CombatStatsAttribute {
            return CombatStatsAttribute(
                    powerProperty = createPropertyFrom(power),
                    toHitProperty = createPropertyFrom(toHit)
            )
        }
    }

}