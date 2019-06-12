package com.jscisco.lom.dungeon.state

import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Response

abstract class State(val dungeon: Dungeon) {

    abstract fun update()

    abstract fun handleInput(context: GameContext): Response

}