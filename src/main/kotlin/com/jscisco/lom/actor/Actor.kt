package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Health
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

abstract class Actor(val tile: Tile, initialPosition: Position3D = Position3D.unknown()) {
    private val positionProperty = createPropertyFrom(initialPosition)
    var position: Position3D by positionProperty.asDelegate()

    abstract val fieldOfView: FieldOfView
    abstract val health: Health
}