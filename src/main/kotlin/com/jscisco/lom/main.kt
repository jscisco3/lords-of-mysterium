package com.jscisco.lom

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.view.StartView
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("Main")

    StartView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())).dock()

//    val application = SwingApplications.startApplication(GameConfiguration.buildAppConfig())
//
//    val visibleSize = Sizes.create3DSize(application.tileGrid.width / 5 * 4, application.tileGrid.height / 6 * 5, 1)
//    val dungeonSize = Size3D.create(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT, 1)
//
//    val dungeon = DungeonBuilder(dungeonSize)
//            .build(visibleSize, dungeonSize)
//
//    logger.info(dungeon.hasBlockAt(Position3D.defaultPosition()).toString())
//    dungeon.setBlockAt(Position3D.defaultPosition(), GameBlock.create())
//    dungeon.fetchBlocks().map {
//        logger.info(it.block.isOccupied.toString())
//    }
//
//    System.exit(0)

}
