package com.jscisco.lom.view

import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
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

        var startButton = Components.button()
                .withText("[N]ew Game?")
                .build()

        var jumpIntoDungeon = Components.button()
                .withText("Jump into dungeon")
                .withPosition(Positions.bottomLeftOf(startButton))
                .build()

        startButton.onMouseClicked {
            KingdomView(tileGrid).dock()
        }

        jumpIntoDungeon.onMouseClicked {
            DungeonView(tileGrid, Dungeon(10)).dock()
        }

        panel.addComponent(startButton)
        panel.addComponent(jumpIntoDungeon)

        screen.addComponent(panel)
    }

}