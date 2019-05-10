package com.jscisco.lom.dungeon.state

import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.zircon.api.uievent.UIEvent

/**
 * This State is responsible for processing entities and takes no input
 * This is the default state
 */
class ProcessingState(dungeon: Dungeon) : State(dungeon) {

    override fun update() {}

    override fun handleInput(input: UIEvent): Response {
        return Pass
    }
}