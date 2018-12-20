package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_WIDTH
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class Dungeon(private val visibleSize: Size3D, val maxDepth: Int) : GameArea<Tile, GameBlock> by GameAreaBuilder<Tile, GameBlock>()
        .withLayersPerBlock(2)
        .withVisibleSize(visibleSize)
        .withActualSize(Size3D.create(WINDOW_WIDTH, WINDOW_HEIGHT, 2))
        .withDefaultBlock(DEFAULT_BLOCK)
        .build() {


    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}