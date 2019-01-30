package com.jscisco.lom.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.concat
import org.hexworks.cobalt.databinding.api.expression.concatWithConvert
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

class HealthAttribute(val maxHpProperty: Property<Int>,
                      val hpProperty: Property<Int> = createPropertyFrom(maxHpProperty.value)) : DisplayableAttribute {

    val maxHp: Int by maxHpProperty.asDelegate()
    val hp: Int by hpProperty.asDelegate()

    /**
     * Executes [fn] when the entity having this attribute
     * should be destroyed according to it's [hp] value
     */
    fun whenShouldBeDestroyed(fn: () -> Unit) {
        if (hp < 1) {
            fn()
        }
    }

    override fun toComponent(width: Int): Component {
        return Components.panel().withSize(width, 2).build().apply {
            val health = Components.label().withSize(width, 1).withPosition(0, 1).build()

            val healthProp = createPropertyFrom("HP: ")
                    .concatWithConvert(hpProperty).concat("/").concatWithConvert(maxHpProperty)

            health.textProperty.bind(healthProp)

            addComponent(health)
        }
    }

    companion object {
        fun create(maxHp: Int) =
                HealthAttribute(maxHpProperty = createPropertyFrom(maxHp))
    }
}