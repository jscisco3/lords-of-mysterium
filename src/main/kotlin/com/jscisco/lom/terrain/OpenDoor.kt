package com.jscisco.lom.terrain

import com.jscisco.lom.builders.GameTileBuilder

class OpenDoor : Terrain(tile = GameTileBuilder.OPEN_DOOR, walkable = true, transparent = true)