package com.jscisco.lom.view

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid

class DungeonView(tileGrid: TileGrid, dungeon: Dungeon) : BaseView(tileGrid) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {

        var gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withVisibleSize(dungeon.visibleSize())
                .withGameArea(dungeon)
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        screen.addComponent(gameComponent)
    }
}