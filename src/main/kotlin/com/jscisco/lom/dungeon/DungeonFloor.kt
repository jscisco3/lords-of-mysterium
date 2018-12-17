package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class DungeonFloor(val width: Int, val height: Int) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
        .withVisibleSize(Size3D.create(20, 20, 1))
        .withActualSize(Size3D.create(200, 200, 1))
        .withDefaultBlock()
        .build(){

    val logger = LoggerFactory.getLogger(javaClass)

    val tiles = Array(width) { Array<Terrain>(height) { Floor() } }

    init {
    }
}