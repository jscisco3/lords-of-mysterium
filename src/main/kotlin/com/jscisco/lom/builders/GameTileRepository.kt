package com.jscisco.lom.builders

import org.hexworks.zircon.api.TileColors
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile

object GameTileRepository {

    val EMPTY: CharacterTile = Tiles.empty()

    fun floor(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('.')
                .withForegroundColor(TileColors.fromString("#FFFFFF"))
                .withBackgroundColor(TileColors.fromString("#000000"))
                .buildCharacterTile()
    }

    fun wall(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('#')
                .withForegroundColor(TileColors.fromString("#FF0000"))
                .withBackgroundColor(TileColors.fromString("#000000"))
                .buildCharacterTile()
    }

    private val DEFAULT = Tiles.newBuilder()
            .withBackgroundColor(TileColors.fromString("#000000"))
            .buildCharacterTile()

    val PLAYER = DEFAULT
            .withCharacter('@')
            .withForegroundColor(TileColors.fromString("#0000FF"))

}