package com.jscisco.lom.dungeon.state

import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.amethyst.api.Response
import org.hexworks.zircon.api.uievent.UIEvent

abstract class State(val dungeon: Dungeon) {

    abstract fun update()

    abstract fun handleInput(input: UIEvent): Response

}