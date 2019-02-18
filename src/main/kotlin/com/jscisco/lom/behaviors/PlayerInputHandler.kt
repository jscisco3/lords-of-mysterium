package com.jscisco.lom.behaviors

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.*
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.input.InputType
import org.hexworks.zircon.api.kotlin.whenKeyStroke

object PlayerInputHandler : BaseBehavior<GameContext>() {

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val (dungeon, screen, input, player) = context
        var response: Response = Pass
        context.input.whenKeyStroke { ks ->
            when (ks.inputType()) {
                InputType.ArrowUp -> response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(-1)))
                InputType.ArrowDown -> response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeY(1)))
                InputType.ArrowLeft -> response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(-1)))
                InputType.ArrowRight -> response = player.executeCommand(MoveCommand(context, player, player.position.withRelativeX(1)))
                else -> return@whenKeyStroke
            }
            when (ks.getCharacter()) {
                ',' -> response = player.executeCommand(PickItemUpCommand(context = context, source = player, position = player.position))
                'i' -> context.screen.openModal(InventoryDialog(context))
                'e' -> context.screen.openModal(EquipmentDialog(context))
                'd' -> if (player.inventory.items.lastOrNull() != null) {
                    response = player.executeCommand(DropItemCommand(context, player, player.inventory.items.last(), player.position))
                }
//                    'z' -> autoexploreMode()
                '>' -> response = player.executeCommand(DescendStairsCommand(context, player))
                '<' -> response = player.executeCommand(AscendStairsCommand(context, player))
                // 't' -> targetingMode()
                else -> return@whenKeyStroke
            }
        }
        return response == Consumed
    }
}