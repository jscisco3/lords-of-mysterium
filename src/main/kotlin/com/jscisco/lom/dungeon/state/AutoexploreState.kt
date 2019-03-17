package com.jscisco.lom.dungeon.state

import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CancelAutoexplore
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon

class AutoexploreState : HeroState {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(context: GameContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleInput(context: GameContext, input: UIEvent) {
        Zircon.eventBus.publish(CancelAutoexplore(context.player))
    }
}