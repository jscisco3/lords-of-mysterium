package com.jscisco.lom.terrain

import com.jscisco.lom.builders.GameTileBuilder

class ClosedDoor : Terrain(tile = GameTileBuilder.CLOSED_DOOR, walkable = false, transparent = false)