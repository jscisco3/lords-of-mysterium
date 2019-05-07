package com.jscisco.lom.view

import com.jscisco.lom.actor.Player
import com.jscisco.lom.configuration.GameConfiguration.VISIBLE_DUNGEON_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.VISIBLE_DUNGEON_WIDTH
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_WIDTH
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.extensions.onMouseEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed

class StartView() : BaseView() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun onDock() {
        val panel = Components.panel()
                .wrapWithBox(wrapWithBox = true)
                .withTitle(title = "Lords of Mysterium")
                .withSize(size = Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT - 2))
                .build()

        var jumpIntoDungeon = Components.button()
                .withText("Jump into dungeon")
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithBox()
                .build()

        jumpIntoDungeon.onMouseEvent(MouseEventType.MOUSE_RELEASED) { _, _ ->
            val dungeonSize = Size3D.create(WINDOW_WIDTH, WINDOW_HEIGHT, 5)
            val visibleSize = Size3D.create(VISIBLE_DUNGEON_WIDTH, VISIBLE_DUNGEON_HEIGHT, 1)

            val dungeon = DungeonBuilder(dungeonSize, strategy = GenericDungeonStrategy(dungeonSize), player = Player())
                    .build(visibleSize, dungeonSize)
//            dungeon.writeToFile()

            replaceWith(DungeonView(dungeon))
            close()
            Processed
        }
        panel.addComponent(jumpIntoDungeon)
        screen.addComponent(panel)
    }

}