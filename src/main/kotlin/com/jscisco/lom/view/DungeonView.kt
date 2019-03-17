package com.jscisco.lom.view

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.attributes.LookingAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.flags.Exploring
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.commands.*
import com.jscisco.lom.configuration.GameConfiguration.LOG_AREA_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.SIDEBAR_WIDTH
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_WIDTH
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CancelAutoexplore
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.events.Targeting
import com.jscisco.lom.events.TargetingCancelled
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.whenHasAttribute
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import com.jscisco.lom.view.fragment.PlayerStatsFragment
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.onKeyboardEvent
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class DungeonView(private val dungeon: Dungeon) : BaseView() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun onDock() {
        val sidebar = Components.panel()
                .withSize(SIDEBAR_WIDTH, WINDOW_HEIGHT)
                .build()
        sidebar.addFragment(PlayerStatsFragment(dungeon.player))

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(dungeon)
                .withVisibleSize(dungeon.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        val logPanel = Components.panel()
                .withSize(WINDOW_WIDTH - SIDEBAR_WIDTH, LOG_AREA_HEIGHT)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .wrapWithBox()
                .withTitle("Journal")
                .build()
        val logArea = Components.logArea()
                .withSize(logPanel.width - 2, logPanel.height - 2)
                .withAlignmentWithin(logPanel, ComponentAlignment.TOP_LEFT)
                .build()
        logPanel.addComponent(logArea)

        /**
         * Subscribe events that are specific for the DungeonView
         */
        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logger.info(text)
            logArea.addParagraph(text, withNewLine = false)
        }

        /**
         * Combine all components to make the screen
         */
        screen.addComponent(sidebar)
        screen.addComponent(gameComponent)
        screen.addComponent(logPanel)

        screen.onKeyboardEvent(KeyboardEventType.KEY_RELEASED) { event, _ ->
            val player = dungeon.player
            val context = GameContext(dungeon = dungeon, screen = screen, player = dungeon.player)
            Zircon.eventBus.publish(CancelAutoexplore(player))
            player.whenHasAttribute<ActiveTurn> {
                player.whenHasAttribute<LookingAttribute> {
                    val lookingAttribute = player.attribute<LookingAttribute>()
                    logger.debug("You are looking at %s".format(lookingAttribute.position.toString()))
                    when (event.code) {
                        KeyCode.UP -> lookingAttribute.position = lookingAttribute.position.withRelativeY(-1)
                        KeyCode.DOWN -> lookingAttribute.position = lookingAttribute.position.withRelativeY(1)
                        KeyCode.LEFT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(-1)
                        KeyCode.RIGHT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(1)
                        KeyCode.ESCAPE -> {
                            player.removeAttribute(lookingAttribute)
                            player.addAttribute(Exploring)
                            Zircon.eventBus.publish(TargetingCancelled())
                        }
                    }
                    Zircon.eventBus.publish(Targeting())
                }
            }
            Processed
        }
    }
}