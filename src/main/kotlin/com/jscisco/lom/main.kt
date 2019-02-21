package com.jscisco.lom

import com.jscisco.lom.attributes.flags.ActiveTurn
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.view.DungeonView
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Size3D
import java.util.*
import kotlin.system.measureTimeMillis

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
