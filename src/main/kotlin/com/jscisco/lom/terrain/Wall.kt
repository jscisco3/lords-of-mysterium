package com.jscisco.lom.terrain

import com.jscisco.lom.builders.GameTileBuilder

class Wall : Terrain(GameTileBuilder.wall(), false, false)