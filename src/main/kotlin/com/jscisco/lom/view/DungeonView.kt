package com.jscisco.lom.view

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
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.input.InputType
import org.hexworks.zircon.api.kotlin.onKeyStroke
import org.hexworks.zircon.api.mvc.base.BaseView
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

        screen.onKeyStroke { ks ->
            val player = dungeon.player
            val context = GameContext(dungeon = dungeon, screen = screen, player = dungeon.player)
            player.whenHasAttribute<ActiveTurn> {
                player.whenHasAttribute<Exploring> {
                    when (ks.inputType()) {
                        InputType.ArrowUp -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(-1)))
                        InputType.ArrowDown -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(1)))
                        InputType.ArrowLeft -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(-1)))
                        InputType.ArrowRight -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(1)))
                    }
                    when (ks.getCharacter()) {
                        ',' -> player.executeCommand(PickItemUpCommand(context = context, source = player, position = player.position))
                        'i' -> context.screen.openModal(InventoryDialog(context))
                        'e' -> context.screen.openModal(EquipmentDialog(context))
                        'd' -> if (player.inventory.items.lastOrNull() != null) {
                            player.executeCommand(DropItemCommand(context, player, player.inventory.items.last(), player.position))
                        }
//                    'z' -> autoexploreMode()
                        '>' -> player.executeCommand(DescendStairsCommand(context, player))
                        '<' -> player.executeCommand(AscendStairsCommand(context, player))
                        // 't' -> targetingMode()
                        'l' -> {
                            player.addAttribute(LookingAttribute(player.position))
                            player.removeAttribute(Exploring)
                        }
                    }
                }
                player.whenHasAttribute<LookingAttribute> {
                    val lookingAttribute = player.attribute<LookingAttribute>()
                    logger.debug("You are looking at %s".format(lookingAttribute.position.toString()))
                    when (ks.inputType()) {
                        InputType.ArrowUp -> lookingAttribute.position = lookingAttribute.position.withRelativeY(-1)
                        InputType.ArrowDown -> lookingAttribute.position = lookingAttribute.position.withRelativeY(1)
                        InputType.ArrowLeft -> lookingAttribute.position = lookingAttribute.position.withRelativeX(-1)
                        InputType.ArrowRight -> lookingAttribute.position = lookingAttribute.position.withRelativeX(1)
                        InputType.Escape -> {
                            player.removeAttribute(lookingAttribute)
                            player.addAttribute(Exploring)
                            Zircon.eventBus.publish(TargetingCancelled())
                        }
                    }
                    Zircon.eventBus.publish(Targeting())
                }
            }
        }

    }
}