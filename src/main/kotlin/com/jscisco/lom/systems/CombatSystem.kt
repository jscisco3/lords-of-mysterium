package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.DestroyCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CombatInstigatedEvent
import com.jscisco.lom.events.EntityDamagedEvent
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.internal.Zircon

object CombatSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, target) ->
        Zircon.eventBus.publish(CombatInstigatedEvent(context, attacker, target))
        var damageAmount = 10
        target.health.hpProperty.value -= damageAmount
        target.health.whenShouldBeDestroyed {
            target.executeCommand(DestroyCommand(context, target, ""))
        }
        Zircon.eventBus.publish(GameLogEvent(
                "%s dealt %s damage to %s. %s health remaining!".format(attacker.entityName, damageAmount, target.entityName, target.health.hpProperty.value)
        ))
        Zircon.eventBus.publish(EntityDamagedEvent(context, attacker, target, damageAmount))
        Consumed
    }
}