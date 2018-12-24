package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.view.StartView
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.SwingApplications

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("Main")

    StartView(SwingApplications.startTileGrid(GameConfiguration.buildAppConfig())).dock()

}
