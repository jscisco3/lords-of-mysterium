package com.jscisco.lom.dungeon.state

import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.PopStateEvent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon

/**
 * We should utilize the AutoexploreBehavior to move automatically.
 */
class AutoexploreState(dungeon: Dungeon, screen: Screen) : State(dungeon, screen) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update() {
    }

    override fun handleInput(input: UIEvent): Response {
        if (input.type == KeyboardEventType.KEY_PRESSED) {
            val event = input as KeyboardEvent
            if (event.code == KeyCode.ESCAPE) {
                Zircon.eventBus.publish(PopStateEvent())
            }
        }
        return Pass
    }
}