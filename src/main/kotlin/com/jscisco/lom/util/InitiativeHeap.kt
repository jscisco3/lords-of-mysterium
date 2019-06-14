package com.jscisco.lom.util

import AnyGameEntity
import com.jscisco.lom.attributes.Initiative
import com.jscisco.lom.extensions.findAttribute
import com.jscisco.lom.extensions.whenHasAttribute

class InitiativeHeap {

    private val size: Int
        get() = heap.size

    private val heap = mutableListOf<AnyGameEntity>()

    // Add an entry to the heap. This is inserted to the end of the heap and then shuffled up appropriately
    fun add(entity: AnyGameEntity) {
        entity.whenHasAttribute<Initiative> {
            heap.add(entity)
            siftUp(size - 1)
        }
    }

    // Return the first entry of the heap. If the heap is empty, return null.
    fun peek(): AnyGameEntity? {
        if (size == 0) {
            return null
        }
        return heap[0]
    }

    // Remove & return the first entry of the heap. If the heap is empty, return null.
    // Update the heap appropriately.
    fun remove(): AnyGameEntity? {
        if (size == 0) {
            return null
        }
        // Get the first item
        val max = peek()
        // Replace the first item with the last item in the heap
        heap[0] = heap.last()
        // Remove the last item
        heap.removeAt(heap.lastIndex)
        // Sift this item to the appropriate place
        siftDown(0)
        return max
    }

    private fun siftDown(idx: Int) {
        // Cannot sift down leaf nodes
        if (idx >= size / 2) {
            return
        }
        // Find the larger child value
        // Left child guaranteed to exist since this is not a leaf node
        // Left node must be filled before right node
        val leftIdx = leftChildIndex(idx)
        val rightIdx = rightChildIndex(idx)
        var smallerChildIdx = leftIdx
        if (rightIdx < size && (heap[rightIdx]!!.findAttribute<Initiative>().cooldown < heap[leftIdx]!!.findAttribute<Initiative>().cooldown)) {
            smallerChildIdx = rightIdx
        }
        // If this is larger than the smaller child, sift it down
        if (heap[idx]!!.findAttribute<Initiative>().cooldown > heap[smallerChildIdx]!!.findAttribute<Initiative>().cooldown) {
            swap(idx, smallerChildIdx)
            siftDown(smallerChildIdx)
        }
    }

    private fun siftUp(idx: Int) {
        // The root node has no parent, so stop sifting up.
        if (idx == 0) {
            return
        }

        val parentIdx = parentIndex(idx)
        // If the entry is smaller than it's parent, we need to swap them.
        if (heap[idx]!!.findAttribute<Initiative>().cooldown < heap[parentIdx]!!.findAttribute<Initiative>().cooldown) {
            swap(idx, parentIdx)
            siftUp(parentIdx)
        }
    }

    private fun parentIndex(idx: Int): Int {
        return idx / 2
    }

    private fun leftChildIndex(idx: Int): Int {
        return idx * 2 + 1
    }

    private fun rightChildIndex(idx: Int): Int {
        return idx * 2 + 2
    }

    private fun swap(left: Int, right: Int) {
        val temp = heap[left]
        heap[left] = heap[right]
        heap[right] = temp
    }

}