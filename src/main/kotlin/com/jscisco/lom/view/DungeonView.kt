package com.jscisco.lom.view

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.GameLogEvent
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.kotlin.onInput
import org.hexworks.zircon.internal.Zircon

class DungeonView(tileGrid: TileGrid, dungeon: Dungeon) : BaseView(tileGrid) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val logAreaHeight = screen.size.height - dungeon.visibleSize().yLength

    init {

        /**
         * Input crap
         */
        screen.onInput {
            dungeon.handleInput(GameContext(
                    dungeon = dungeon,
                    screen = screen,
                    input = it,
                    player = dungeon.player
            ))
//            dungeon.handleInput(it)
//            dungeon.updateFOV()
        }

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(dungeon)
                .withVisibleSize(dungeon.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        val logPanel = Components.panel()
                .withSize(Sizes.create(dungeon.visibleSize().xLength, logAreaHeight))
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .wrapWithBox()
                .withTitle("Journal")
                .build()
        val logArea = Components.logArea()
                .withSize(logPanel.size - Sizes.create(2, 2))
                .build()
        logPanel.addComponent(logArea)


        /**
         * Subscribe events
         */
        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logArea.addParagraph(text, withNewLine = false)
        }

        /**
         * Combine all components to make the screen
         */
        screen.addComponent(gameComponent)
        screen.addComponent(logPanel)
    }


}