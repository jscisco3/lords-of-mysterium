package com.jscisco.lom.systems.combat

import com.jscisco.lom.attributes.types.ev
import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.attributes.types.toHit
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.DestroyCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object CombatSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, defender) ->
        //  From the attacker, get the total toHit
        //  From the defender, get the total EV (evasion
        if ((0..attacker.toHit).random() > (0..defender.ev).random()) {
            // We have a hit!
        } else {
            // We have a miss!
        }

        defender.health.whenShouldBeDestroyed {
            defender.executeCommand(DestroyCommand(context, defender, ""))
        }
        Consumed
    }
}