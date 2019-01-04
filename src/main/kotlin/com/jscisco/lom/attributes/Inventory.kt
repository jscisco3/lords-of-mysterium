package com.jscisco.lom.attributes

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.weight
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.Maybe

data class Inventory(val maxWeight: Int) : Attribute {

    private val currentItems = mutableListOf<GameEntity<Item>>()

    val items: List<GameEntity<Item>>
        get() = currentItems.toList()

    val isEmpty: Boolean
        get() = currentItems.isEmpty()

    val currentWeight: Int
        get() = currentItems.map { it.weight }.sum()

    fun findItemById(id: Identifier): Maybe<GameEntity<Item>> {
        return Maybe.ofNullable(items.firstOrNull { it.id == id })
    }

    /**
     * Try to add an item to the current inventory. Return false if not added
     */
    fun addItem(item: GameEntity<Item>): Boolean {
        return if (((currentWeight + item.weight) > maxWeight).not()) {
            currentItems.add(item)
        } else false
    }

    /**
     * Remove an [Item] from this [Inventory]
     * @return `true` if the item is present, `false` otherwise
     */
    fun removeItem(item: GameEntity<Item>): Boolean {
        return currentItems.remove(item)
    }


}