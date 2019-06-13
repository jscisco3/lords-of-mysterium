package com.jscisco.lom.util

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestMinHeap {

    private lateinit var minHeap: MinHeap<Int>

    @BeforeEach
    fun init() {
        minHeap = MinHeap()
    }

    @Test
    fun `When I peek an empty heap, it should return null`() {
        val entry = minHeap.peek()
        Assertions.assertThat(entry).isNull()
    }

    @Test
    fun `After I add an entry to a new heap, I should see it when I peek`() {
        val entry = 5
        minHeap.add(entry)
        Assertions.assertThat(minHeap.peek()).isEqualTo(entry)
    }

    @Test
    fun `If I remove from an empty heap, I should get null`() {
        val entry = minHeap.remove()
        Assertions.assertThat(entry).isNull()
    }

    @Test
    fun `Removing an entry for a heap with only one element should return that element and leave it empty`() {
        minHeap.add(10)
        val entry = minHeap.remove()
        Assertions.assertThat(entry).isEqualTo(10)
        Assertions.assertThat(minHeap.peek()).isNull()
    }

    @Test
    fun `Adding a larger element will not change the top of the heap`() {
        val FIRST = 100
        minHeap.add(FIRST)
        minHeap.add(FIRST * 1000)
        Assertions.assertThat(minHeap.peek()).isEqualTo(FIRST)
    }

    @Test
    fun `Adding a smaller element will have it be sifted to the top of the heap`() {
        minHeap.add(100)
        minHeap.add(50)
        Assertions.assertThat(minHeap.peek()).isEqualTo(50)
    }

    @Test
    fun `Removing the top element of a multi-element heap will make the top of the heap correct`() {
        val first = 100
        val second = 150
        val third = 200
        minHeap.add(first)
        minHeap.add(second)
        minHeap.add(third)
        Assertions.assertThat(minHeap.peek()).isEqualTo(first)
        val entry = minHeap.remove()
        Assertions.assertThat(entry).isEqualTo(first)
        Assertions.assertThat(minHeap.peek()).isEqualTo(second)
    }

    @Test
    fun `Adding elements out of order and then removing them should leave them sorted`() {
        minHeap.add(1)
        minHeap.add(-10)
        minHeap.add(100)
        minHeap.add(100)
        minHeap.add(150)
        minHeap.add(30)
        minHeap.add(0)

        val list = mutableListOf<Int>()

        while (minHeap.peek() != null) {
            print(list.size)
            list.add(minHeap.remove()!!)
        }
        Assertions.assertThat(list).isSorted
    }
}