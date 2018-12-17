package com.jscisco.lom.blocks

import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = TerrainRepository.floor()) : BlockBase<Tile>() {
    override val layers: MutableList<Tile>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun fetchSide(side: BlockSide): Tile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun create(): GameBlock = GameBlock()
    }
}