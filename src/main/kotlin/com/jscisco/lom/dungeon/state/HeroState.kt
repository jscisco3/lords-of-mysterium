package com.jscisco.lom.dungeon.state

import com.jscisco.lom.dungeon.GameContext
import org.hexworks.zircon.api.uievent.UIEvent

interface HeroState {

    fun update(context: GameContext)

    fun handleInput(context: GameContext, input: UIEvent)

}