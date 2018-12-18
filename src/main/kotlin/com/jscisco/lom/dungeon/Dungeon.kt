package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockRepository
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class Dungeon(startingBlocks: Map<Position3D, GameBlock>,
              visibleSize: Size3D,
              actualSize: Size3D) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .withDefaultBlock(DEFAULT_BLOCK)
        .withLayersPerBlock(2)
        .build() {

    init {

    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockRepository.floor()
    }
}