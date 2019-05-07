package com.jscisco.lom.terrain

import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Tile

abstract class Terrain(val tile: Tile = Tiles.empty(), val walkable: Boolean, val transparent: Boolean)