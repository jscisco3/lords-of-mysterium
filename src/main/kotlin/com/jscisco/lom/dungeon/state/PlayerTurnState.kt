package com.jscisco.lom.dungeon.state

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.*
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.extensions.position
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon

/**
 * This state is responsible for handling the player's turn
 * It doesn't run any updates, but it does handle the player's input
 */
class PlayerTurnState : HeroState {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(context: GameContext) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return
    }

    override fun handleInput(context: GameContext, input: UIEvent) {
        // Keyboard events
        val player = context.player
        var response: Response = Pass
        if (input.type == KeyboardEventType.KEY_PRESSED) {
            val event = input as KeyboardEvent
            logger.info(input.toString())
            when (event.code) {
                KeyCode.UP -> {
                    response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(-1)))
                }
                KeyCode.DOWN -> {
                    response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(1)))
                }
                KeyCode.LEFT -> {
                    response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(-1)))
                }
                KeyCode.RIGHT -> {
                    response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(1)))
                }
                KeyCode.ESCAPE -> {
                    if (event.shiftDown) {
                        Zircon.eventBus.publish(QuitGameEvent())
                    }
                }
            }
            when (event.key) {
                "," -> {
                    response = player.executeCommand(PickItemUpCommand(context = context, source = player, position = player.position))
                }
                "i" -> context.screen.openModal(InventoryDialog(context))
                "e" -> context.screen.openModal(EquipmentDialog(context))
                "d" -> if (player.inventory.items.lastOrNull() != null) {
                    response = player.executeCommand(DropItemCommand(context, player, player.inventory.items.last(), player.position))
                }
                "z" -> {
                    // Add autoexplore attribute so the behavior works as expected
                    player.addAttribute(AutoexploreAttribute())
                    // Push a new state so that the correct update call is made
                    context.dungeon.pushState(AutoexploreState())
//                    player.update(context)
                }
                ">" -> {
                    response = player.executeCommand(DescendStairsCommand(context, player))
                }
                "<" -> {
                    response = player.executeCommand(AscendStairsCommand(context, player))
                }
                // 't' -> targetingMode()
                "l" -> {
//                    player.addAttribute(LookingAttribute(player.position))
//                    player.removeAttribute(Exploring)
                }
            }
            logger.info(response.toString())
            if (response == Consumed) {
                player.executeCommand(EndTurnCommand(context, player))
                context.dungeon.popState()
            }
        }
    }
}