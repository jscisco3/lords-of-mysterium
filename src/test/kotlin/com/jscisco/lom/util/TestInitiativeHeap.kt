package com.jscisco.lom.util

import AnyGameEntity
import com.jscisco.lom.attributes.Initiative
import com.jscisco.lom.attributes.types.NPC
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.extensions.newGameEntityOfType
import org.assertj.core.api.Assertions
import org.hexworks.amethyst.api.base.BaseEntityType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestInitiativeHeap {

    private lateinit var initiativeHeap: InitiativeHeap

    @BeforeEach
    fun init() {
        initiativeHeap = InitiativeHeap()
    }

    @Test
    fun `When I peek an empty heap, it should return null`() {
        val entry = initiativeHeap.peek()
        Assertions.assertThat(entry).isNull()
    }

    @Test
    fun `After I add an entry to a new heap, I should see it when I peek`() {
        val entry = newTestEntity(100)
        initiativeHeap.add(entry)
        Assertions.assertThat(initiativeHeap.peek()).isEqualTo(entry)
    }

    @Test
    fun `If I remove from an empty heap, I should get null`() {
        val entry = initiativeHeap.remove()
        Assertions.assertThat(entry).isNull()
    }

    @Test
    fun `Removing an entry for a heap with only one element should return that element and leave it empty`() {
        val entry = newTestEntity(10)
        initiativeHeap.add(entry)
        Assertions.assertThat(initiativeHeap.remove()).isEqualTo(entry)
        Assertions.assertThat(initiativeHeap.peek()).isNull()
    }

    @Test
    fun `Adding a larger element will not change the top of the heap`() {
        val FIRST = newTestEntity(100)
        initiativeHeap.add(FIRST)
        initiativeHeap.add(newTestEntity(10000))
        Assertions.assertThat(initiativeHeap.peek()).isEqualTo(FIRST)
    }

    @Test
    fun `Adding a smaller element will have it be sifted to the top of the heap`() {
        val oneHundred = newTestEntity(100)
        val fifty = newTestEntity(50)
        initiativeHeap.add(oneHundred)
        initiativeHeap.add(fifty)
        Assertions.assertThat(initiativeHeap.peek()).isEqualTo(fifty)
    }

    @Test
    fun `Removing the top element of a multi-element heap will make the top of the heap correct`() {
        val first = newTestEntity(100)
        val second = newTestEntity(150, Player)
        val third = newTestEntity(200)
        initiativeHeap.add(first)
        initiativeHeap.add(second)
        initiativeHeap.add(third)
        Assertions.assertThat(initiativeHeap.peek()).isEqualTo(first)
        val entry = initiativeHeap.remove()
        Assertions.assertThat(entry).isEqualTo(first)
        Assertions.assertThat(initiativeHeap.peek()).isEqualTo(second)
    }

    private fun newTestEntity(initialCooldown: Int, type: BaseEntityType = NPC): AnyGameEntity {
        return newGameEntityOfType(type) {
            attributes(
                    Initiative(initialCooldown)
            )
        }
    }

}