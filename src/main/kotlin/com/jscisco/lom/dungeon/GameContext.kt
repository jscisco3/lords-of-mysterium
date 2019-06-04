package com.jscisco.lom.dungeon

import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent

class GameContext(val dungeon: Dungeon,
                  val screen: Screen,
                  val uiEvent: UIEvent) : Context {
}