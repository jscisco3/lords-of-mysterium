package com.jscisco.lom.view

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.kotlin.onInput
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

        screen.onInput {
            logger.info(it.toString())
        }

        startButton.onMouseClicked {
            KingdomView(tileGrid).dock()
        }

        panel.addComponent(startButton)

        screen.addComponent(panel)
    }

}