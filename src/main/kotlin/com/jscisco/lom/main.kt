package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.view.DungeonView
import com.jscisco.lom.view.StartView
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.grid.TileGrid

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("Main")

    StartView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())).dock()

}
