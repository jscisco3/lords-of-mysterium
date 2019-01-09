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
import org.hexworks.zircon.api.kotlin.onMouseReleased
import org.hexworks.zircon.api.mvc.base.BaseView

class StartView() : BaseView() {

    private val logger = LoggerFactory.getLogger(StartView::class.java)

    override fun onDock() {
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

        jumpIntoDungeon.onMouseReleased {
            val dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 1)
            val visibleSize = Sizes.create3DSize(screen.width / 5 * 4, screen.height / 6 * 5, 1)

            val dungeon = DungeonBuilder(dungeonSize, strategy = EmptyDungeonStrategy(dungeonSize), player = EntityFactory.newPlayer())
                    .build(visibleSize, dungeonSize)

            replaceWith(DungeonView(dungeon))
            close()
        }
        panel.addComponent(jumpIntoDungeon)
        screen.addComponent(panel)
    }

}