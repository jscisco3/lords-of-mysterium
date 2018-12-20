package com.jscisco.lom.configuration

import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig
import java.awt.GraphicsEnvironment

object GameConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val SCREEN_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds

    //    private val SCREEN_SIZE = Toolkit.getDefaultToolkit().screenSize
    private const val SCREEN_SIZE_PERCENT = 0.7
    private const val FULL_SCREEN = false

    val TILESET = CP437TilesetResources.rogueYun16x16()

    val WINDOW_WIDTH = SCREEN_SIZE.width.div(TILESET.width).times(SCREEN_SIZE_PERCENT).toInt()
    val WINDOW_HEIGHT = SCREEN_SIZE.height.div(TILESET.height).times(SCREEN_SIZE_PERCENT).toInt()

    val DUNGEON_SIZE = Sizes.create3DSize(WINDOW_WIDTH * 1, WINDOW_HEIGHT * 1, 1)

    fun buildAppConfig(): AppConfig {
        logger.info("WINDOW_WIDTH: %s | WINDOW_HEIGHT: %s | DUNGEON_SIZE: %s".format(WINDOW_WIDTH, WINDOW_HEIGHT, DUNGEON_SIZE))
        val config = AppConfigs.newConfig()
                .enableBetaFeatures()
                .withDefaultTileset(TILESET)
//                .withDebugMode(true)
        if (FULL_SCREEN) {
            config.fullScreen()
        } else {
            config.withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        }
        return config.build()
    }

}