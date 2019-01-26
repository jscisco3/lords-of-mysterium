package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class Health(val maxHpProperty: Property<Int>,
             val hpProperty: Property<Int> = createPropertyFrom(maxHpProperty.value)) : Attribute {

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

    companion object {
        fun create(maxHp: Int) =
                Health(maxHpProperty = createPropertyFrom(maxHp))
    }
}