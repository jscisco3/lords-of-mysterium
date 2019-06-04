package com.jscisco.lom.facets

import GameCommand
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.internal.Zircon

object Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context, attacker, defender) ->
        val defenderHealth = defender.findAttribute(Health::class)
        defenderHealth.ifPresent {
            it.hp -= 5
        }
        Zircon.eventBus.publish(GameLogEvent("${attacker.name} attacked the ${defender.name}"))
        defenderHealth.ifPresent {
            it.whenShouldBeDestroyed {
                context.dungeon.removeEntity(defender)
                Zircon.eventBus.publish(GameLogEvent("${defender.name} is defeated!"))
            }
        }
        Consumed
    }
}