package com.jscisco.lom.configuration

import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig

object GameConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private const val FULL_SCREEN = false

    const val SIDEBAR_WIDTH = 18
    const val LOG_AREA_HEIGHT = 12

    const val WINDOW_HEIGHT = 50
    const val WINDOW_WIDTH = 80
    const val VISIBLE_DUNGEON_HEIGHT = WINDOW_HEIGHT - LOG_AREA_HEIGHT
    const val VISIBLE_DUNGEON_WIDTH = WINDOW_WIDTH - SIDEBAR_WIDTH

    val DUNGEON_SIZE = Sizes.create3DSize(WINDOW_WIDTH, WINDOW_HEIGHT, 1)


    val TILESET = CP437TilesetResources.rogueYun16x16()

    val THEME = ColorThemes.adriftInDreams()

    fun buildAppConfig(): AppConfig {
        logger.debug("WINDOW_WIDTH: %s | WINDOW_HEIGHT: %s | DUNGEON_SIZE: %s".format(WINDOW_WIDTH, WINDOW_HEIGHT, DUNGEON_SIZE))
        val config = AppConfigs.newConfig()
                .enableBetaFeatures()
                .withDebugMode(false)
                .withDefaultTileset(TILESET)
        if (FULL_SCREEN) {
            config.fullScreen()
        } else {
            config.withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
        }
        return config.build()
    }

}