package com.jscisco.lom.dungeon.state

import com.jscisco.lom.actor.Player
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.QuitGameEvent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon

/**
 * This state is responsible for handling the player's turn
 * It doesn't run any updates, but it does handle the player's input
 */
class PlayerTurnState(dungeon: Dungeon, screen: Screen) : State(dungeon, screen) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val player: Player = dungeon.player

    override fun update() {

    }

    override fun handleInput(input: UIEvent): Response {
        // Keyboard events
        if (input.type == KeyboardEventType.KEY_PRESSED) {
            val event = input as KeyboardEvent
            when (event.code) {
                KeyCode.UP -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeY(-1)).let {
                        it.invoke()
                    }
                }
                KeyCode.DOWN -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeY(1)).let {
                        it.invoke()
                    }
                }
                KeyCode.LEFT -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeX(-1)).let {
                        it.invoke()
                    }
                }
                KeyCode.RIGHT -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeX(1)).let {
                        it.invoke()
                    }
                }
                KeyCode.ESCAPE -> {
                    if (event.shiftDown) {
                        Zircon.eventBus.publish(QuitGameEvent())
                    }
                }
            }
            when (event.key) {
                "," -> {
//                    response = player.executeCommand(PickItemUpCommand(context = context, source = player, position = player.position))
                }
//                "i" -> context.screen.openModal(InventoryDialog(context))
//                "e" -> context.screen.openModal(EquipmentDialog(context))
//                "d" -> if (player.inventory.items.lastOrNull() != null) {
//                    response = player.executeCommand(DropItemCommand(context, player, player.inventory.items.last(), player.position))
//                }
                "z" -> {
                    // Push a new state so that the correct update call is made
//                    context.dungeon.pushState(AutoexploreState())
//                    // Update the player so the Autoexplore behavior runs.
//                    player.update(context)
                }
                ">" -> {
//                    response = player.executeCommand(DescendStairsCommand(context, player))
                }
                "<" -> {
//                    response = player.executeCommand(AscendStairsCommand(context, player))
                }
                // 't' -> targetingMode()
                "l" -> {
//                    player.addAttribute(LookingAttribute(player.position))
//                    player.removeAttribute(Exploring)
                }
            }
        }
        return Pass
    }
}