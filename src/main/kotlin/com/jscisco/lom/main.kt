package com.jscisco.lom

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.view.DungeonView
import com.jscisco.lom.view.StartView
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("Main")

//    StartView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())).dock()

    val application = SwingApplications.startApplication(GameConfiguration.buildAppConfig())

    val visibleSize = Sizes.create3DSize(application.tileGrid.width / 5 * 4, application.tileGrid.height / 6 * 5, 1)
    val dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 1)

    val hero = EntityFactory.newPlayer()

    val dungeon = DungeonBuilder(dungeonSize, hero = hero)
            .build(visibleSize, dungeonSize)

    DungeonView(application.tileGrid, dungeon).dock()
}
