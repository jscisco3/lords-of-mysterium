package com.jscisco.lom.blocks

import com.jscisco.lom.builders.GameTileRepository
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(var defaultTile: Tile = GameTileRepository.floor()) : BlockBase<Tile>() {
    override val layers: MutableList<Tile>
        get() = mutableListOf(defaultTile, GameTileRepository.EMPTY)

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileRepository.EMPTY
    }

    companion object {
        fun create(): GameBlock = GameBlock()

        fun createWith(tile: Tile) = GameBlock(tile)

    }
}