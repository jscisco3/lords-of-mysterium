package com.jscisco.lom.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

class Health(initalHp: Int = 100) : DisplayableAttribute {

    private val maxHpProperty: Property<Int> = createPropertyFrom(initalHp)
    private val hpProperty: Property<Int> = createPropertyFrom(initalHp)
    var hp: Int by hpProperty.asDelegate()
    var maxHp: Int by maxHpProperty.asDelegate()

    fun whenShouldBeDestroyed(fn: () -> Unit) {
        if (hp < 1) {
            fn()
        }
    }

    override fun toComponent(width: Int): Component {
        return Components.panel().withSize(width, 5).build().apply {
            val health = Components.label().withSize(width, 1).withPosition(0, 1).build()
            val healthProp = createPropertyFrom("HP: ")
            health.textProperty.bind(healthProp)

            addComponent(health)
        }
    }
}