package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.EquippableAttribute
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.builders.EntityFactory.NO_EQUIPMENT
import com.jscisco.lom.commands.EquipItemCommand
import com.jscisco.lom.commands.UnequipItemCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.newGameEntityOfType
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.hexworks.amethyst.api.base.BaseEntityType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestEquipUnequipItemSystems {

    private lateinit var entity: GameEntity<ItemHolder>
    private val testItem: GameEntity<Item> = testItem()
    private val testItem2: GameEntity<Item> = testItem()
    private val testEquipmentSlot: EquipmentSlot = EquipmentSlot.create(EquipmentType.HAND)
    private val context: GameContext = mockk()

    @BeforeEach
    fun init() {
        entity = newGameEntityOfType(TestEntityType) {
            attributes(
                    Inventory(maxWeight = 100),
                    EquipmentAttribute(
                            listOf(testEquipmentSlot)
                    )
            )
            facets(
                    EquipItemSystem,
                    UnequipItemSystem
            )
        }
    }

    @Test
    fun `equip item to empty slot`() {
        entity.executeCommand(EquipItemCommand(
                context = context,
                source = entity,
                item = testItem,
                equipmentSlot = testEquipmentSlot
        ))
        Assertions.assertThat(testEquipmentSlot.equippedItem).isEqualTo(testItem)
    }

    @Test
    fun `unequipping an item should add it to the inventory`() {
        testEquipmentSlot.equippedItem = testItem
        entity.executeCommand(UnequipItemCommand(
                context = context,
                source = entity,
                equipmentSlot = testEquipmentSlot
        ))
        Assertions.assertThat(entity.inventory.items).contains(testItem)
        Assertions.assertThat(testEquipmentSlot.equippedItem).isEqualTo(NO_EQUIPMENT)
    }

    @Test
    fun `equipping an item to an occupied slot should unequip the occupying item and replace it with the new one`() {
        testEquipmentSlot.equippedItem = testItem
        entity.executeCommand(EquipItemCommand(
                context = mockk(),
                source = entity,
                item = testItem2,
                equipmentSlot = testEquipmentSlot
        ))
        Assertions.assertThat(testEquipmentSlot.equippedItem).isEqualTo(testItem2)
        Assertions.assertThat(entity.inventory.items).contains(testItem)

    }

    companion object {
        object TestEntityType : BaseEntityType(), ItemHolder

        fun testItem() = newGameEntityOfType(Item) {
            attributes(
                    EquippableAttribute(EquipmentType.HAND)
            )
        }
    }

}