package com.jscisco.lom.systems.combat

import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.DestroyCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.DamageEvent
import com.jscisco.lom.events.MissEvent
import com.jscisco.lom.events.OnHitEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon
import java.lang.Integer.max

object CombatSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, defender) ->
        //  From the attacker, get the total toHit
        //  From the defender, get the total EV (evasion
        if (hitSuccessful(attacker, defender)) {
            Zircon.eventBus.publish(OnHitEvent(context, attacker, defender))
            val damage = calculateDamage(attacker, defender)
            defender.health.hpProperty.value -= damage
            Zircon.eventBus.publish(DamageEvent(context, attacker, defender, damage))
        } else {
            Zircon.eventBus.publish(MissEvent(context, attacker, defender))
        }

        defender.health.whenShouldBeDestroyed {
            defender.executeCommand(DestroyCommand(context, defender, ""))
        }
        Consumed
    }

    private fun hitSuccessful(attacker: GameEntity<Combatant>, defender: GameEntity<Combatant>): Boolean {
        val hitCalc = ((0..attacker.toHit).random() > (0..defender.ev).random())
        val attackerToHit = (0..attacker.toHit).random()
        val defenderEv = (0..defender.ev).random()
        logger.info("%s attacked %s and had %s toHit vs their %s ev".format(attacker, defender, attackerToHit, defenderEv))
        return hitCalc
    }

    private fun calculateDamage(attacker: GameEntity<Combatant>, defender: GameEntity<Combatant>): Int {
        val damage = max(1, (0..attacker.attackPower).random() - (0..defender.ac).random())
        logger.info("%s dealt %s damage to %s.".format(attacker, damage, defender))
        return damage
    }
}