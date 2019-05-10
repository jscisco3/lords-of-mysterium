package com.jscisco.lom.dungeon.state

import com.jscisco.lom.commands.Pass
import com.jscisco.lom.commands.Response
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.zircon.api.uievent.UIEvent

class TargetingState(dungeon: Dungeon) : State(dungeon) {

    override fun update() {}

    override fun handleInput(input: UIEvent): Response {
//        val player = context.player
//        val lookingAttribute = player.attribute<LookingAttribute>()
//        if (input.type == KeyboardEventType.KEY_RELEASED) {
//            val event = input as KeyboardEvent
//            when (event.code) {
//                KeyCode.UP -> lookingAttribute.position = lookingAttribute.position.withRelativeY(-1)
//                KeyCode.DOWN -> lookingAttribute.position = lookingAttribute.position.withRelativeY(1)
//                KeyCode.LEFT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(-1)
//                KeyCode.RIGHT -> lookingAttribute.position = lookingAttribute.position.withRelativeX(1)
//                KeyCode.ESCAPE -> {
//                    player.removeAttribute(lookingAttribute)
//                    player.addAttribute(Exploring)
//                    Zircon.eventBus.publish(TargetingCancelled())
//                }
//            }
//            Zircon.eventBus.publish(Targeting())
//        }
        return Pass
    }
}