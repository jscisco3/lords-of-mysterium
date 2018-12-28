package com.jscisco.lom.view

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.graphics.BoxType
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
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithBox()
                .build()

        jumpIntoDungeon.onMouseClicked {
            val dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 1)
            val visibleSize = Sizes.create3DSize(tileGrid.width / 5 * 4, tileGrid.height / 6 * 5, 1)

            val hero = EntityFactory.newPlayer()

            val dungeon = DungeonBuilder(dungeonSize, hero = hero, strategy = EmptyDungeonStrategy(dungeonSize))
                    .build(visibleSize, dungeonSize)

            DungeonView(tileGrid, dungeon).dock()
        }

        panel.addComponent(jumpIntoDungeon)

        screen.addComponent(panel)
    }

}