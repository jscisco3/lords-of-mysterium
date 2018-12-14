package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.view.StartView
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig

fun main(args: Array<String>) {
    StartView(SwingApplications.startTileGrid(appConfig = GameConfiguration.buildAppConfig())).dock()
}
