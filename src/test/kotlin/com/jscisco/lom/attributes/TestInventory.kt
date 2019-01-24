package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.builders.EntityFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestInventory {

    @Test
    fun testInventoryAddItem() {
        val player = EntityFactory.newPlayer()
        val sword = EntityFactory.newSword()
        player.inventory.addItem(sword)
        Assertions.assertThat(player.inventory.items.size).isEqualTo(1)
    }

    @Test
    fun testInventoryRemoveItemAfterAddingIt() {
        val player = EntityFactory.newPlayer()
        val sword = EntityFactory.newSword()
        player.inventory.addItem(sword)
        Assertions.assertThat(player.inventory.items.size).isEqualTo(1)
        val removed = player.inventory.removeItem(sword)
        Assertions.assertThat(removed).isTrue()
    }

    @Test
    fun testInventoryRemoveItemButIsNotPresent() {
        val player = EntityFactory.newPlayer()
        val removed = player.inventory.removeItem(EntityFactory.newSword())
        Assertions.assertThat(removed).isFalse()
    }

}