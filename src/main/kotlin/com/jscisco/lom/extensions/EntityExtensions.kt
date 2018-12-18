package com.jscisco.lom.extensions

import com.jscisco.lom.entities.Entity
import org.hexworks.zircon.api.data.Tile

val Entity.tile: Tile
    get() = metadata.tile