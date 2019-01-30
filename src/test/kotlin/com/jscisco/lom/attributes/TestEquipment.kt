package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.Equipment.EquipmentSlot
import com.jscisco.lom.attributes.Equipment.EquipmentType
import com.jscisco.lom.attributes.types.NoItem
import com.jscisco.lom.builders.EntityFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestEquipment {

    @Test
    fun testInitializeEquipmentNoInitialEquipment() {
        val eligibleSlots = listOf(
                EquipmentSlot(EquipmentType.HAND),
                EquipmentSlot(EquipmentType.HEAD)
        )
        val equipment: Equipment = Equipment(eligibleSlots)

        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND).get(0).type).isEqualTo(NoItem)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.BODY).size).isEqualTo(0)
    }

    @Test
    fun testInitializeEquipmentWithInitialEquipment() {
        val sword = EntityFactory.newSword()

        val eligibleSlots = listOf(
                EquipmentSlot(EquipmentType.HAND).also {
                    it.equippedItem = sword
                },
                EquipmentSlot(EquipmentType.HEAD)
        )

        val equipment: Equipment = Equipment(eligibleSlots)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND).get(0)).isEqualTo(sword)
    }

    @Test
    fun testEquipItem() {
        val eligibleSlots = listOf(
                EquipmentSlot(EquipmentType.HAND),
                EquipmentSlot(EquipmentType.HEAD)
        )
        val equipment: Equipment = Equipment(eligibleSlots)
        val sword = EntityFactory.newSword()

        val inventory = Inventory(100)

        equipment.equipItem(inventory, eligibleSlots.filter { it.type == EquipmentType.HAND }[0], sword)

        Assertions.assertThat(equipment.getSlotsByType(EquipmentType.HAND)[0].equippedItem).isEqualTo(sword)
    }

    @Test
    fun testEquipItemIfItemAlreadyEquipped() {
        val eligibleSlots = listOf(
                EquipmentSlot(EquipmentType.HAND),
                EquipmentSlot(EquipmentType.HEAD)
        )
        val equipment = Equipment(eligibleSlots)
        val sword1 = EntityFactory.newSword()
        val sword2 = EntityFactory.newSword()

        val inventory = Inventory(100)

        equipment.equipItem(inventory, equipment.equipment.filter { it.type == EquipmentType.HAND }[0], sword1)
        Assertions.assertThat(inventory.items.size).isEqualTo(0)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND)[0]).isEqualTo(sword1)

        equipment.equipItem(inventory, equipment.equipment.filter { it.type == EquipmentType.HAND }[0], sword2)
        // The sword should be put back in to the inventory
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND)[0]).isEqualTo(sword2)
        Assertions.assertThat(inventory.items.last()).isEqualTo(sword1)
    }
}