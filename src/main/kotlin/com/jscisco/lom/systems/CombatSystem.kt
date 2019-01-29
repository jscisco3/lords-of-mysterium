package com.jscisco.lom.systems

import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.InstigateCombatEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.internal.Zircon

object CombatSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, target) ->
        //        val attackerEquipment = attacker.equipment
//        val attackerStats = attacker.combatStats
//
//        val targetEquipment = target.equipment
//        val targetStats = target.combatStats

        Zircon.eventBus.publish(InstigateCombatEvent(context, attacker, target))

        Consumed
    }
}