package com.jscisco.lom.entities.attributes

import org.hexworks.zircon.api.data.Tile

data class EntityMetadata(val tile: Tile = Tile.empty(), override val name: String = "unknown") : Attribute