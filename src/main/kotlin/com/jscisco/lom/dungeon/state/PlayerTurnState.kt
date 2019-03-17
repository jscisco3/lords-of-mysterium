package com.jscisco.lom.dungeon.state

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.attributes.LookingAttribute
import com.jscisco.lom.attributes.flags.Exploring
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.*
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent

class PlayerTurnState : HeroState {

    override fun update(context: GameContext) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return
    }

    override fun handleInput(context: GameContext, input: UIEvent) {
        // Keyboard events
        val player = context.player
        if (input.type == KeyboardEventType.KEY_RELEASED) {
            val event = input as KeyboardEvent
            when (event.code) {
                KeyCode.UP -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(-1)))
                KeyCode.DOWN -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(1)))
                KeyCode.LEFT -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(-1)))
                KeyCode.RIGHT -> player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(1)))
            }
            when (event.key) {
                "," -> player.executeCommand(PickItemUpCommand(context = context, source = player, position = player.position))
                "i" -> context.screen.openModal(InventoryDialog(context))
                "e" -> context.screen.openModal(EquipmentDialog(context))
                "d" -> if (player.inventory.items.lastOrNull() != null) {
                    player.executeCommand(DropItemCommand(context, player, player.inventory.items.last(), player.position))
                }
                "z" -> {
                    // Add autoexplore attribute so the behavior works as expected
                    player.addAttribute(AutoexploreAttribute())
                    context.dungeon.pushState(AutoexploreState())
                    player.update(context)
                }
                ">" -> player.executeCommand(DescendStairsCommand(context, player))
                "<" -> player.executeCommand(AscendStairsCommand(context, player))
                // 't' -> targetingMode()
                "l" -> {
                    player.addAttribute(LookingAttribute(player.position))
                    player.removeAttribute(Exploring)
                }
            }
            context.dungeon.popState()
        }
    }
}