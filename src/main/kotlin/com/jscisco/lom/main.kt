package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.view.DungeonView
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.grid.TileGrid

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("Main")

//    StartView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())).dock()


//    DungeonView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig()), dungeon).dock()
    val grid: TileGrid = SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())
    val dungeon: Dungeon = DungeonBuilder(GameConfiguration.DUNGEON_SIZE, grid.size).build()
    DungeonView(grid, dungeon).dock()
}
