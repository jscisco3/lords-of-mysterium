package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.StatBlockAttribute
import com.jscisco.lom.extensions.newGameEntityOfType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestCombatantType {

    val ITEM_WITHOUT_SBA = newGameEntityOfType(Item) {
        attributes()
    }

    val ITEM_WITH_SBA = newGameEntityOfType(Item) {
        attributes(
                StatBlockAttribute.create(
                        toHit = 5
                )
        )
    }

    val TEST_ENTITY_NO_EQUIPMENT = newGameEntityOfType(CombatantType) {
        attributes(
                StatBlockAttribute.create(
                        toHit = 5
                )
        )
    }

    val TEST_ENTITY_WITH_ITEMS_NO_SBA = newGameEntityOfType(CombatantType) {
        attributes(
                StatBlockAttribute.create(
                        toHit = 5
                ),
                EquipmentAttribute(
                        listOf(EquipmentSlot.create(EquipmentType.HEAD, ITEM_WITHOUT_SBA))
                )
        )
    }

    val TEST_ENTITY_WITH_ITEMS_WITH_SBA = newGameEntityOfType(CombatantType) {
        attributes(
                StatBlockAttribute.create(toHit = 5),
                EquipmentAttribute(listOf(EquipmentSlot.create(EquipmentType.HEAD, ITEM_WITH_SBA)))
        )
    }

    @Test
    fun `should be able to get toHit without any equipped items`() {
        Assertions.assertThat(TEST_ENTITY_NO_EQUIPMENT.toHit).isEqualTo(5)
    }

    @Test
    fun `should be able to get toHit with some equipped items that don't have StatBlockAttribute`() {
        Assertions.assertThat(TEST_ENTITY_WITH_ITEMS_NO_SBA.toHit).isEqualTo(5)
    }

    @Test
    fun `should be able to get toHit with some equipped items that have a StatBlockAttribute`() {
        Assertions.assertThat(TEST_ENTITY_WITH_ITEMS_WITH_SBA.toHit).isEqualTo(10)
    }


}