package com.jscisco.lom.configuration

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig
import java.awt.GraphicsEnvironment
import java.awt.Toolkit

object GameConfiguration {
    private val SCREEN_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds

    private const val SCREEN_SIZE_PERCENT = 0.6
    private const val FULL_SCREEN = false

    val TILESET = CP437TilesetResources.rogueYun16x16()


    val WINDOW_WIDTH = SCREEN_SIZE.width.div(TILESET.width).times(SCREEN_SIZE_PERCENT).toInt()
    val WINDOW_HEIGHT = SCREEN_SIZE.height.div(TILESET.height).times(SCREEN_SIZE_PERCENT).toInt()

    fun buildAppConfig(): AppConfig {
        val config = AppConfigs.newConfig()
                .enableBetaFeatures()
                .withDefaultTileset(TILESET)
        if (FULL_SCREEN) {
            config.fullScreen()
        } else {
            config.withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        }
        return config.build()
    }

}