package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class EnergyAttribute(val energyProperty: Property<Int>,
                      val rechargeRateProperty: Property<Int>) : Attribute {

    val energy: Int by energyProperty.asDelegate()
    val rechargeRate: Int by rechargeRateProperty.asDelegate()

    fun recharge() {
        energyProperty.value += rechargeRate
    }

    companion object {
        fun create(energy: Int = 1000, rechargeRate: Int = 1000): EnergyAttribute {
            return EnergyAttribute(createPropertyFrom(energy),
                    createPropertyFrom(rechargeRate))
        }
    }

}