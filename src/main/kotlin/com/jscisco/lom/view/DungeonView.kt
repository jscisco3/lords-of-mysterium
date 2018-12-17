package com.jscisco.lom.view

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid

class DungeonView(tileGrid: TileGrid, dungeon: Dungeon) : BaseView(tileGrid) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    val visibleSize = Sizes.create3DSize(120 / 5 * 4, 120 / 6 * 5, 1)


    init {

        var gameComponent = Components.gameComponent<Tile, GameBlock>()
                .withVisibleSize(visibleSize)
                .withGameArea()
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        screen.addComponent(gameComponent)

//        for (dungeonFloor in dungeon.dungeonFloors) {
//            logger.info(dungeonFloor.height.toString())
//        }
    }
}