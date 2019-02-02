package com.jscisco.lom.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.concatWithConvert
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

data class StatBlockAttribute(
        val strengthProperty: Property<Int>,
        val intelligenceProperty: Property<Int>,
        val constitutionProperty: Property<Int>,
        val agilityProperty: Property<Int>,
        val perceptionProperty: Property<Int>
) : DisplayableAttribute {

    operator fun plus(statBlockAttribute: StatBlockAttribute) {
        strengthProperty.value += statBlockAttribute.strengthProperty.value
        intelligenceProperty.value += statBlockAttribute.intelligenceProperty.value
        constitutionProperty.value += statBlockAttribute.constitutionProperty.value
        agilityProperty.value += statBlockAttribute.agilityProperty.value
        perceptionProperty.value += statBlockAttribute.perceptionProperty.value
    }

    operator fun minus(statBlockAttribute: StatBlockAttribute) {
        strengthProperty.value -= statBlockAttribute.strengthProperty.value
        intelligenceProperty.value -= statBlockAttribute.intelligenceProperty.value
        constitutionProperty.value -= statBlockAttribute.constitutionProperty.value
        agilityProperty.value -= statBlockAttribute.agilityProperty.value
        perceptionProperty.value -= statBlockAttribute.perceptionProperty.value
    }

    override fun toComponent(width: Int): Component {
        return Components.panel().withSize(width, 5).build().apply {
            val strengthLabel = Components.label().withSize(width, 1).withPosition(0, 0).build()
            val strengthProp = createPropertyFrom("S: ")
                    .concatWithConvert(strengthProperty)
            strengthLabel.textProperty.bind(strengthProp)

            val intelligenceLabel = Components.label().withSize(width, 1).withPosition(0, 1).build()
            val intelligenceProp = createPropertyFrom("I: ")
                    .concatWithConvert(intelligenceProperty)
            intelligenceLabel.textProperty.bind(intelligenceProp)

            val constitutionLabel = Components.label().withSize(width, 1).withPosition(0, 2).build()
            val constitutionProp = createPropertyFrom("C: ")
                    .concatWithConvert(constitutionProperty)
            constitutionLabel.textProperty.bind(constitutionProp)

            val agilityLabel = Components.label().withSize(width, 1).withPosition(0, 3).build()
            val agilityProp = createPropertyFrom("A: ")
                    .concatWithConvert(agilityProperty)
            agilityLabel.textProperty.bind(agilityProp)

            val perceptionLabel = Components.label().withSize(width, 1).withPosition(0, 4).build()
            val perceptionProp = createPropertyFrom("P: ")
                    .concatWithConvert(perceptionProperty)
            perceptionLabel.textProperty.bind(perceptionProp)



            addComponent(strengthLabel)
            addComponent(intelligenceLabel)
            addComponent(constitutionLabel)
            addComponent(agilityLabel)
            addComponent(perceptionLabel)
        }
    }

    companion object {
        fun create(strength: Int = 0,
                   intelligence: Int = 0,
                   constitution: Int = 0,
                   agility: Int = 0,
                   perception: Int = 0) =
                StatBlockAttribute(
                        strengthProperty = createPropertyFrom(strength),
                        intelligenceProperty = createPropertyFrom(intelligence),
                        constitutionProperty = createPropertyFrom(constitution),
                        agilityProperty = createPropertyFrom(agility),
                        perceptionProperty = createPropertyFrom(perception)
                )
    }
}