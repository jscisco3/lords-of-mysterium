package com.jscisco.lom

import com.jscisco.lom.actor.Player
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.state.PlayerTurnState
import com.jscisco.lom.dungeon.state.State
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.events.PopStateEvent
import com.jscisco.lom.events.PushStateEvent
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.view.DungeonView
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.extensions.onKeyboardEvent
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

    val dungeon = DungeonBuilder(dungeonSize, strategy = GenericDungeonStrategy(dungeonSize), player = Player())
            .build(visibleSize, dungeonSize)

    val dv = DungeonView(dungeon)
    application.dock(dv)

    var playing = true

    val states = mutableListOf<State>(PlayerTurnState(dungeon, dv.screen))

    Zircon.eventBus.subscribe<QuitGameEvent> {
        playing = false
        logger.info("quitting game")
    }

    Zircon.eventBus.subscribe<PopStateEvent> {
        logger.trace("Popping state ${states.last()}")
        states.removeAt(states.lastIndex)
        logger.info("Current state is now ${states.last()}")
    }

    Zircon.eventBus.subscribe<PushStateEvent> {
        states.add(it.state)
    }

    dv.screen.onKeyboardEvent(KeyboardEventType.KEY_PRESSED) { event, _ ->
        logger.info(states.last().toString())
        states.last().handleInput(event)
        Processed
    }

    while (playing) {
        try {
            states.last().update()
        } catch (e: Exception) {
            logger.error(e.localizedMessage ?: "Unknown")
        }
    }
    System.exit(0)
}
