package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class InitiativeAttribute(val initiativeProperty: Property<Int>) : Attribute {

    val initiative: Int by initiativeProperty.asDelegate()

    fun recharge() {
        initiativeProperty.value += 100
    }

    companion object {
        fun create(initiative: Int = 100): InitiativeAttribute {
            return InitiativeAttribute(createPropertyFrom(initiative))
        }
    }

}