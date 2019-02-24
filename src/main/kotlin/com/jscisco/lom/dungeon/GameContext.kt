package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.api.screen.Screen


data class GameContext(val dungeon: Dungeon,
                       val screen: Screen,
                       val player: GameEntity<Player>) : Context