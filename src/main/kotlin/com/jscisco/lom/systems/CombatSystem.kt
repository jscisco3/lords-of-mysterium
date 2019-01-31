package com.jscisco.lom.systems

import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CombatInstigatedEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.internal.Zircon

object CombatSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, target) ->
        Zircon.eventBus.publish(CombatInstigatedEvent(context, attacker, target))
        Consumed
    }
}