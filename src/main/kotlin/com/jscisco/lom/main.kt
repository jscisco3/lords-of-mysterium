package com.jscisco.lom

import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.view.StartView
import org.hexworks.zircon.api.SwingApplications
import java.util.*

fun main(args: Array<String>) {
    System.setProperty("session.id", UUID.randomUUID().toString())
    val application = SwingApplications.startApplication(GameConfiguration.buildAppConfig())
    application.dock(StartView())
}
