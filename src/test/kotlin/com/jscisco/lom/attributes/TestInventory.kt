package com.jscisco.lom.attributes

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.whenHasAttribute
import org.assertj.core.api.Assertions
import org.junit.Test

class TestInventory {

    @Test
    fun testInventoryAddItem() {
        val entity = EntityFactory.newPlayer()
        val sword = EntityFactory.newSword()

        entity.whenHasAttribute<Inventory> {
            it.addItem(sword)
            Assertions.assertThat(it.currentWeight).isNotEqualTo(0)
        }
    }

    @Test
    fun testInventoryAddItemButInventoryIsFull() {
        val player = EntityFactory.newPlayer()

        player.whenHasAttribute<Inventory> {
            while (it.currentWeight < it.maxWeight) {
                it.addItem(EntityFactory.newSword())
            }
            val itemAdded = it.addItem(EntityFactory.newSword())
            Assertions.assertThat(itemAdded).isFalse()
        }
    }

    @Test
    fun testInventoryRemoveItemAfterAddingIt() {
        val player = EntityFactory.newPlayer()

        player.whenHasAttribute<Inventory> {
            val sword = EntityFactory.newSword()
            it.addItem(sword)
            val removed = it.removeItem(sword)
            Assertions.assertThat(removed).isTrue()
        }
    }

    @Test
    fun testInventoryRemoveItemButIsNotPresent() {
        val player = EntityFactory.newPlayer()

        player.whenHasAttribute<Inventory> {
            val removed = it.removeItem(EntityFactory.newSword())
            Assertions.assertThat(removed).isFalse()
        }
    }

}