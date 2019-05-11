package com.jscisco.lom.dungeon.state

import com.jscisco.lom.actor.Player
import com.jscisco.lom.commands.*
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.PushStateEvent
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.events.dialog.OpenEquipmentDialog
import com.jscisco.lom.events.dialog.OpenInventoryDialog
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
class PlayerTurnState(dungeon: Dungeon) : State(dungeon) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val player: Player = dungeon.player

    override fun update() {}

    override fun handleInput(input: UIEvent): Response {
        // Keyboard events
        if (input.type == KeyboardEventType.KEY_PRESSED) {
            val event = input as KeyboardEvent
            when (event.code) {
                KeyCode.UP -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeY(-1)).invoke()
                }
                KeyCode.DOWN -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeY(1)).invoke()
                }
                KeyCode.LEFT -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeX(-1)).invoke()
                }
                KeyCode.RIGHT -> {
                    return MoveCommand(dungeon, player, player.position.withRelativeX(1)).invoke()
                }
                KeyCode.ESCAPE -> {
                    if (event.shiftDown) {
                        Zircon.eventBus.publish(QuitGameEvent())
                    }
                }
            }
            when (event.key) {
                "," -> {
                    return PickItemUpCommand(dungeon, player, player.position).invoke()
                }
                "i" -> {
                    Zircon.eventBus.publish(OpenInventoryDialog(dungeon, player))
                }
                "e" -> {
                    Zircon.eventBus.publish(OpenEquipmentDialog(player))
                }
                "d" -> if (player.inventory.items.isEmpty().not()) {
                    return DropItemCommand(dungeon, player, player.inventory.items[0]).invoke()
                }
                "z" -> {
                    // Push a new state so that the correct update call is made
                    val autoExploreState = AutoexploreState(dungeon)
                    Zircon.eventBus.publish(PushStateEvent(autoExploreState))
                }
                ">" -> {
//                    response = player.executeCommand(DescendStairsCommand(context, player))
                }
                "<" -> {
//                    response = player.executeCommand(AscendStairsCommand(context, player))
                }
                "V" -> {
                    player.health.hp -= 5
                }
                "l" -> {
//                    player.addAttribute(LookingAttribute(player.position))
//                    player.removeAttribute(Exploring)
                }
            }
        }
        return Pass
    }
}