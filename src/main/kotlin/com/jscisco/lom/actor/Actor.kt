package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

abstract class Actor(val tile: Tile, var position: Position3D) {
    abstract val fieldOfView: FieldOfView
}