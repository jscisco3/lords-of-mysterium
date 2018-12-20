package com.jscisco.lom.view

import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.kotlin.onMouseClicked

class StartView(tileGrid: TileGrid) : BaseView(tileGrid) {

    private val logger = LoggerFactory.getLogger(StartView::class.java)

    init {
        val panel = Components.panel()
                .wrapWithBox(wrapWithBox = true)
                .withTitle(title = "Lords of Mysterium")
                .withSize(size = Sizes.create(30, 30))
                .build()

        var jumpIntoDungeon = Components.button()
                .withText("Jump into dungeon")
                .build()

        jumpIntoDungeon.onMouseClicked {
            val visibleSize = Sizes.create3DSize(tileGrid.width / 5 * 4, tileGrid.height / 6 * 5, 1)
            val dungeon: Dungeon = Dungeon(visibleSize, 5)
            DungeonView(tileGrid, dungeon).dock()
        }

        panel.addComponent(jumpIntoDungeon)

        screen.addComponent(panel)
    }

}