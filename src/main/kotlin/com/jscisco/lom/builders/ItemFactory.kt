package com.jscisco.lom.builders

import com.jscisco.lom.capabilities.Attack
import com.jscisco.lom.item.Item
import org.hexworks.cobalt.datatypes.Maybe

object ItemFactory {

    fun sword(): Item {
        return Item(tile = GameTileBuilder.SWORD, name = "Sword").also {
            it.attack = Maybe.of(Attack(5, 10))
        }
    }

}