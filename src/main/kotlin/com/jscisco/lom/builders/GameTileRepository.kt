package com.jscisco.lom.builders

import org.hexworks.zircon.api.TileColors
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile

object GameTileRepository {

    val EMPTY: CharacterTile = Tiles.empty()

    fun floor(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('.')
                .withForegroundColor(TileColors.fromString("#0E0E0E"))
                .withBackgroundColor(TileColors.fromString("#000000"))
                .buildCharacterTile()
    }
}