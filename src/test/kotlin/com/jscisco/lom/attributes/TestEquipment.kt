package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import org.assertj.core.api.Assertions
import org.junit.Test

class TestEquipment {

    @Test
    fun testInitializeEquipmentNoInitialEquipment() {
        val eligibleSlots = listOf(EquipmentSlot.ONE_HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD)
        val equipment: Equipment = Equipment(eligibleSlots, mutableMapOf())

        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isNull()
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.BODY)).isNull()
    }

    @Test
    fun testInitializeEquipmentWithInitialEquipment() {
        val eligibleSlots = listOf(EquipmentSlot.ONE_HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD)
        val initialEquipment: MutableMap<EquipmentSlot, GameEntity<Item>?> = mutableMapOf()
        initialEquipment[EquipmentSlot.ONE_HAND] = EntityFactory.newSword()

        val equipment: Equipment = Equipment(eligibleSlots, initialEquipment)
    }

}