package com.jscisco.lom.dungeon.state

import com.jscisco.lom.attributes.LookingAttribute
import com.jscisco.lom.attributes.flags.Exploring
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.Targeting
import com.jscisco.lom.events.TargetingCancelled
import com.jscisco.lom.extensions.attribute
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.internal.Zircon

class TargetingState : HeroState {

    override fun update(context: GameContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleInput(context: GameContext, input: UIEvent) {
        val player = context.player
        val lookingAttribute = player.attribute<LookingAttribute>()
        if (input.type == KeyboardEventType.KEY_RELEASED) {
            val event = input as KeyboardEvent
            when (event.code) {
                KeyCode.UP -> lookingAttribute.position = lookingAttribute.position.withRelativeY(-1)
                KeyCode.DOWN -> lookingAttribute.position = lookingAttribute.position.withRelativeY(1)
                KeyCode.LEFT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(-1)
                KeyCode.RIGHT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(1)
                KeyCode.ESCAPE -> {
                    player.removeAttribute(lookingAttribute)
                    player.addAttribute(Exploring)
                    Zircon.eventBus.publish(TargetingCancelled())
                }
            }
            Zircon.eventBus.publish(Targeting())
        }
    }
}