package com.jscisco.lom.dungeon.state

import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent

/**
 * We should utilize the AutoexploreBehavior to move automatically.
 */
class AutoexploreState(dungeon: Dungeon, screen: Screen) : State(dungeon, screen) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update() {
    }

    override fun handleInput(input: UIEvent): Response {
        return Pass
    }
}