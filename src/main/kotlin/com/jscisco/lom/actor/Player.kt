package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.builders.GameTileBuilder

class Player : Actor(tile = GameTileBuilder.PLAYER) {
    override val fieldOfView: FieldOfView = FieldOfView(10.0)
    val inventory: Inventory = Inventory()
}