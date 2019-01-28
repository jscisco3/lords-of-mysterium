package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        val sword = EntityFactory.newSword()
        initialEquipment[EquipmentSlot.ONE_HAND] = sword

        val equipment: Equipment = Equipment(eligibleSlots, initialEquipment)
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(sword)
    }

    @Test
    fun testEquipItem() {
        val eligibleSlots = listOf(EquipmentSlot.ONE_HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD)
        val equipment = Equipment(eligibleSlots, mutableMapOf())
        val sword = EntityFactory.newSword()

        val inventory = Inventory(100)

        equipment.equipItem(inventory, EquipmentSlot.ONE_HAND, sword)

        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(sword)
    }

    @Test
    fun testEquipItemIfItemAlreadyEquipped() {
        val eligibleSlots = listOf(EquipmentSlot.ONE_HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD)
        val equipment = Equipment(eligibleSlots, mutableMapOf())
        val sword1 = EntityFactory.newSword()
        val sword2 = EntityFactory.newSword()

        val inventory = Inventory(100)

        equipment.equipItem(inventory, EquipmentSlot.ONE_HAND, sword1)
        Assertions.assertThat(inventory.items.size).isEqualTo(0)
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(sword1)

        equipment.equipItem(inventory, EquipmentSlot.ONE_HAND, sword2)
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(sword2)
        Assertions.assertThat(inventory.items.last()).isEqualTo(sword1)
    }

    @Test
    fun testUnequipItem() {
        val equipment = Equipment(listOf(EquipmentSlot.ONE_HAND), mutableMapOf())
        val sword = EntityFactory.newSword()
        val inventory = Inventory(100)

        equipment.equipItem(inventory, EquipmentSlot.ONE_HAND, sword)
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(sword)

        equipment.unequipItem(inventory, EquipmentSlot.ONE_HAND)
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
        Assertions.assertThat(equipment.getItemBySlot(EquipmentSlot.ONE_HAND)).isEqualTo(null)

    }

}