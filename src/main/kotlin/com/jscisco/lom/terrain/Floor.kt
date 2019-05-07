package com.jscisco.lom.terrain

import com.jscisco.lom.builders.GameTileBuilder

class Floor : Terrain(GameTileBuilder.floor(), true, true)