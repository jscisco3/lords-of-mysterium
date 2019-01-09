package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.view.StartView
import org.hexworks.zircon.api.SwingApplications

fun main(args: Array<String>) {
    val application = SwingApplications.startApplication(GameConfiguration.buildAppConfig())
    application.dock(StartView())
}
