package com.jscisco.lom.dungeon.state

import GameEntity
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.internal.Zircon

/**
 * This state is responsible for handling the player's turn
 * It doesn't run any updates, but it does handle the player's input
 */
class PlayerTurnState(dungeon: Dungeon) : State(dungeon) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val player: GameEntity<Player> = dungeon.player

    override fun update() {}

    override fun handleInput(context: GameContext): Response {
        // Keyboard events
        if (context.uiEvent.type == KeyboardEventType.KEY_PRESSED) {
            val event = context.uiEvent as KeyboardEvent
            when (event.code) {
                KeyCode.UP -> {
                    player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(-1)))
                    context.dungeon.updateCamera()
                }
                KeyCode.DOWN -> {
                    player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(1)))
                    context.dungeon.updateCamera()
                }
                KeyCode.LEFT -> {
                    player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(-1)))
                    context.dungeon.updateCamera()
                }
                KeyCode.RIGHT -> {
                    player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(1)))
                    context.dungeon.updateCamera()
                }
                KeyCode.ESCAPE -> {
                    if (event.shiftDown) {
                        Zircon.eventBus.publish(QuitGameEvent())
                    }
                }
            }
//            when (event.key) {
//                "," -> {
//                    return PickItemUpCommand(dungeon, player, player.position).invoke()
//                }
//                "i" -> {
//                    Zircon.eventBus.publish(OpenInventoryDialog(dungeon, player))
//                }
//                "e" -> {
//                    Zircon.eventBus.publish(OpenEquipmentDialog(player))
//                }
//                "d" -> if (player.inventory.items.isEmpty().not()) {
//                    return DropItemCommand(dungeon, player, player.inventory.items[0]).invoke()
//                }
//                "z" -> {
//                    // Push a new state so that the correct update call is made
//                    val autoExploreState = AutoexploreState(dungeon)
//                    Zircon.eventBus.publish(PushStateEvent(autoExploreState))
//                }
//                ">" -> {
////                    response = player.executeCommand(DescendStairsCommand(context, player))
//                }
//                "<" -> {
////                    response = player.executeCommand(AscendStairsCommand(context, player))
//                }
//                "V" -> {
//                    player.health.hp -= 5
//                }
//                "l" -> {
////                    player.addAttribute(LookingAttribute(player.position))
////                    player.removeAttribute(Exploring)
//                }
//            }
        }
        return Pass
    }
}