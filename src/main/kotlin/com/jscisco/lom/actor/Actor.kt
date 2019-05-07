package com.jscisco.lom.actor

import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

abstract class Actor(val tile: Tile, var position: Position3D) {
}