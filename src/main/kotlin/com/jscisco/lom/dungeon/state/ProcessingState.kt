package com.jscisco.lom.dungeon.state

import com.jscisco.lom.dungeon.GameContext
import org.hexworks.zircon.api.uievent.UIEvent

/**
 * This State is responsible for processing entities and takes no input
 * This is the default state
 */
class ProcessingState : HeroState {

    override fun update(context: GameContext) {
        context.dungeon.update(context.screen)
    }

    override fun handleInput(context: GameContext, input: UIEvent) {
        // Processing state is just responsible for making sure normal behaviors are running
    }
}