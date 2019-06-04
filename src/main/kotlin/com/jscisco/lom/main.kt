package com.jscisco.lom

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.view.DungeonView
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
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

//    val states = mutableListOf<State>(ProcessingState(dungeon))

    Zircon.eventBus.subscribe<QuitGameEvent> {
        System.exit(0)
    }

//    Zircon.eventBus.subscribe<StartPlayerTurn> {
//        Zircon.eventBus.publish(PushStateEvent(PlayerTurnState(dungeon)))
//    }

//    Zircon.eventBus.subscribe<PopStateEvent> {
//        logger.info("Popping state ${states.last()}")
//        states.removeAt(states.lastIndex)
//        logger.info("Current state is now ${states.last()}")
//    }

//    Zircon.eventBus.subscribe<PushStateEvent> {
//        states.add(it.state)
//        logger.info(states.last().toString())
//    }

//    Zircon.eventBus.subscribe<OpenInventoryDialog> {
//        dv.screen.openModal(InventoryDialog(it.dungeon, it.player, dv.screen))
//    }
//
//    Zircon.eventBus.subscribe<OpenEquipmentDialog> {
//        dv.screen.openModal(EquipmentDialog(it.player, dv.screen))
//    }

    dv.screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
//        states.last().handleInput(event)
        Processed
    }

    while (true) {
//        states.last()?.update()
    }
}
