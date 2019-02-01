package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.EquipmentSlot
import com.jscisco.lom.attributes.types.EquipmentType
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.EntityFactory.NO_EQUIPMENT
import com.jscisco.lom.builders.EntityFactory.NO_ITEM
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.newGameEntityOfType
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestEquipmentAttribute {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun testInitializeEquipmentNoInitialEquipment() {
        val eligibleSlots = listOf(
                EquipmentSlot.create(EquipmentType.HAND),
                EquipmentSlot.create(EquipmentType.HEAD)
        )
        val equipmentAttribute: EquipmentAttribute = EquipmentAttribute(eligibleSlots)

        Assertions.assertThat(equipmentAttribute.getItemsByType(EquipmentType.HAND).get(0).entityName).isEqualTo(NO_EQUIPMENT.entityName)
        Assertions.assertThat(equipmentAttribute.getItemsByType(EquipmentType.BODY).size).isEqualTo(0)
    }

    @Test
    fun testInitializeEquipmentWithInitialEquipment() {
        val sword = EntityFactory.newSword()

        val eligibleSlots = listOf(
                EquipmentSlot.create(EquipmentType.HAND).also {
                    it.equippedItem = sword
                },
                EquipmentSlot.create(EquipmentType.HEAD)
        )

        val equipmentAttribute: EquipmentAttribute = EquipmentAttribute(eligibleSlots)
        Assertions.assertThat(equipmentAttribute.getItemsByType(EquipmentType.HAND).get(0)).isEqualTo(sword)
    }

    @Test
    fun testEquipItem() {
        val eligibleSlots = listOf(
                EquipmentSlot.create(EquipmentType.HAND),
                EquipmentSlot.create(EquipmentType.HEAD)
        )
        val equipmentAttribute: EquipmentAttribute = EquipmentAttribute(eligibleSlots)
        val sword = EntityFactory.newSword()

        val inventory = Inventory(100)

        equipmentAttribute.equipItemToSlot(inventory, eligibleSlots.filter { it.type == EquipmentType.HAND }[0], sword)

        Assertions.assertThat(equipmentAttribute.getSlotsByType(EquipmentType.HAND)[0].equippedItem).isEqualTo(sword)
    }

    @Test
    fun testEquipItemIfItemAlreadyEquipped() {

        val eqSlotHand = EquipmentSlot.create(EquipmentType.HAND)
        val eqSlotHead = EquipmentSlot.create(EquipmentType.HEAD)

        val eligibleSlots = listOf(
                eqSlotHand,
                eqSlotHead
        )
        val equipment = EquipmentAttribute(eligibleSlots)

        val sword1 = newGameEntityOfType(Item) {
            attributes(
                    NameAttribute("Sword 1"),
                    EquippableAttribute(EquipmentType.HAND)
            )
        }
        val sword2 = newGameEntityOfType(Item) {
            attributes(
                    NameAttribute("Sword 2"),
                    EquippableAttribute(EquipmentType.HAND)
            )
        }

        val inventory = Inventory(100)

        equipment.equipItemToSlot(inventory, eqSlotHand, sword1)
        Assertions.assertThat(inventory.items.size).isEqualTo(0)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND)[0]).isEqualTo(sword1)

        equipment.equipItemToSlot(inventory, eqSlotHand, sword2)

        // The sword should be put back in to the inventory
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
        Assertions.assertThat(equipment.getItemsByType(EquipmentType.HAND)[0]).isEqualTo(sword2)
        Assertions.assertThat(inventory.items.last()).isEqualTo(sword1)
    }
}