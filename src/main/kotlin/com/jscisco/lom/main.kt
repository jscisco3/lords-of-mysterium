package com.jscisco.lom

import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.events.QuitGameEvent
import com.jscisco.lom.view.DungeonView
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Size3D
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

    var playing = true

    Zircon.eventBus.subscribe<QuitGameEvent> {
        playing = false
        logger.info("quitting game")
    }

    while (playing) {
        try {
            dungeon.currentState.update(GameContext(
                    dungeon = dungeon,
                    screen = dv.screen,
                    player = dungeon.player
            ))
//            logger.info(dungeon.currentState.toString())
//            if (dungeon.player.hasAttribute<ActiveTurn>().not() || dungeon.player.hasAttribute<AutoexploreAttribute>()) {
//                dungeon.update(dv.screen)
//            }
        } catch (e: Exception) {
            logger.error(e.localizedMessage ?: "Unknown")
        }
    }
    System.exit(0)
}
