package com.jscisco.lom.attributes

import com.jscisco.lom.builders.EntityFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestInventory {

    private lateinit var inventory: Inventory

    @BeforeEach
    fun init() {
        inventory = Inventory(maxWeight = 100)
    }

    @Test
    fun testInventoryAddItem() {
        val sword = EntityFactory.newSword()
        inventory.addItem(sword)
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
    }

    @Test
    fun testInventoryRemoveItemAfterAddingIt() {
        val sword = EntityFactory.newSword()
        inventory.addItem(sword)
        Assertions.assertThat(inventory.items.size).isEqualTo(1)
        val removed = inventory.removeItem(sword)
        Assertions.assertThat(removed).isTrue()
    }

    @Test
    fun testInventoryRemoveItemButIsNotPresent() {
        val removed = inventory.removeItem(EntityFactory.newSword())
        Assertions.assertThat(removed).isFalse()
    }

}