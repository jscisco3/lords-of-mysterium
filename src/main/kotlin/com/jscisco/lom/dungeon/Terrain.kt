package com.jscisco.lom.dungeon

interface Terrain {
    val walkable: Boolean
    val blocksSight: Boolean
    val character: Char
}

data class Floor(override val walkable: Boolean = true, override val blocksSight: Boolean = true, override val character: Char = '.') : Terrain