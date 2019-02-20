package com.jscisco.lom.builders

import com.jscisco.lom.configuration.GameColors.DEFAULT_BACKGROUND
import com.jscisco.lom.configuration.GameColors.DEFAULT_FOREGROUND
import com.jscisco.lom.configuration.GameColors.FLOOR_BACKGROUND
import com.jscisco.lom.configuration.GameColors.FLOOR_FOREGROUND
import org.hexworks.zircon.api.TileColors
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile

object GameTileBuilder {

    fun floor(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('.')
                .withForegroundColor(FLOOR_FOREGROUND)
                .withBackgroundColor(FLOOR_BACKGROUND)
                .buildCharacterTile()
    }

    fun wall(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('#')
                .withForegroundColor(DEFAULT_FOREGROUND)
                .withBackgroundColor(DEFAULT_BACKGROUND)
                .buildCharacterTile()
    }

    fun sword(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter(')')
                .withForegroundColor(DEFAULT_FOREGROUND)
                .withBackgroundColor(DEFAULT_BACKGROUND)
                .buildCharacterTile()
    }

    fun closedDoor(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('+')
                .withForegroundColor(TileColors.fromString("#603c27"))
                .buildCharacterTile()
    }

    fun openDoor(): CharacterTile {
        return Tiles.newBuilder()
                .withCharacter('/')
                .withForegroundColor(TileColors.fromString("#603c27"))
                .buildCharacterTile()
    }

    val PLAYER: CharacterTile = Tiles.newBuilder()
            .withCharacter('@')
            .withForegroundColor(TileColors.fromString("#FF0000"))
            .withBackgroundColor(DEFAULT_BACKGROUND)
            .buildCharacterTile()

    val GOBLIN: CharacterTile = Tiles.newBuilder()
            .withCharacter('g')
            .withForegroundColor(TileColors.fromString("#00FF00"))
            .buildCharacterTile()

    val STAIRS_UP: CharacterTile = Tiles.newBuilder()
            .withCharacter('<')
            .withForegroundColor(DEFAULT_FOREGROUND)
            .buildCharacterTile()

    val STAIRS_DOWN: CharacterTile = Tiles.newBuilder()
            .withCharacter('>')
            .withForegroundColor(DEFAULT_FOREGROUND)
            .buildCharacterTile()

    val DEFAULT: CharacterTile = Tiles.newBuilder()
            .withBackgroundColor(FLOOR_BACKGROUND)
            .buildCharacterTile()

    val EMPTY: CharacterTile = Tiles.empty()

    val LOOKING_LINE: CharacterTile = Tiles.newBuilder()
            .withBackgroundColor(TileColors.create(244, 241, 66, 192))
            .withCharacter(' ')
            .buildCharacterTile()

    val LOOKING_LINE_BLOCKED: CharacterTile = Tiles.newBuilder()
            .withBackgroundColor(TileColors.create(244, 12, 12, 128))
            .withCharacter(' ')
            .buildCharacterTile()

    val UNREVEALED: CharacterTile = Tiles.newBuilder()
            .withBackgroundColor(DEFAULT_BACKGROUND)
            .withCharacter(' ')
            .buildCharacterTile()

    val SEEN_OUT_OF_SIGHT: CharacterTile = Tiles.newBuilder()
            .withBackgroundColor(TileColors.create(131, 132, 135, 192))
            .withCharacter(' ')
            .buildCharacterTile()
}