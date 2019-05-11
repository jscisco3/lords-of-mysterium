package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.builders.GameTileBuilder
import org.hexworks.zircon.api.data.impl.Position3D

class Player : Actor(tile = GameTileBuilder.PLAYER, position = Position3D.defaultPosition()) {
    override val fieldOfView: FieldOfView = FieldOfView(10.0)
    val inventory: Inventory = Inventory()
}