package com.jscisco.lom

import com.jscisco.lom.attributes.LookingAttribute
import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.commands.*
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.events.Looking
import com.jscisco.lom.events.StopLooking
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.position
import com.jscisco.lom.view.DungeonView
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.InputType
import org.hexworks.zircon.api.kotlin.onKeyStroke
import org.hexworks.zircon.internal.Zircon
import java.util.*

fun main(args: Array<String>) {
    System.setProperty("session.id", UUID.randomUUID().toString())

    val logger: Logger = LoggerFactory.getLogger("com.jscisco.lom.Main")

    val application = SwingApplications.startApplication(GameConfiguration.buildAppConfig())
//    application.dock(StartView())
    val dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 5)
    val visibleSize = Size3D.create(GameConfiguration.VISIBLE_DUNGEON_WIDTH, GameConfiguration.VISIBLE_DUNGEON_HEIGHT, 1)

    val dungeon = DungeonBuilder(dungeonSize, strategy = GenericDungeonStrategy(dungeonSize), player = EntityFactory.newPlayer())
            .build(visibleSize, dungeonSize)

    val dv = DungeonView(dungeon)
    application.dock(dv)

    /**
     * Controls
     */
    dv.screen.onKeyStroke { ks ->
        val context = GameContext(dungeon = dungeon, screen = dv.screen, player = dungeon.player)
        val (dungeon, screen, player) = context
        if (dungeon.gameState == "exploring") {
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
                    dungeon.gameState = "looking"
                    logger.info("Let's look around...")
                    player.addAttribute(LookingAttribute(player.position))
                }
            }
        }
        if (dungeon.gameState == "looking") {
            val lookingAttribute = player.attribute<LookingAttribute>()
            logger.info("You are looking at %s".format(lookingAttribute.position.toString()))
            when (ks.inputType()) {
                InputType.ArrowUp -> lookingAttribute.position = lookingAttribute.position.withRelativeY(-1)
                InputType.ArrowDown -> lookingAttribute.position = lookingAttribute.position.withRelativeY(1)
                InputType.ArrowLeft -> lookingAttribute.position = lookingAttribute.position.withRelativeX(-1)
                InputType.ArrowRight -> lookingAttribute.position = lookingAttribute.position.withRelativeX(1)
                InputType.Escape -> {
                    dungeon.gameState = "exploring"
                    player.removeAttribute(lookingAttribute)
                    Zircon.eventBus.publish(StopLooking())
                }
            }
            Zircon.eventBus.publish(Looking())
        }

    }


    while (true) {
        try {
            if (dungeon.player.hasAttribute<ActiveTurn>().not()) {
                dungeon.update(dv.screen)
            }
        } catch (e: Exception) {
            logger.error(e.message ?: "Unknown")
        }

    }
}
