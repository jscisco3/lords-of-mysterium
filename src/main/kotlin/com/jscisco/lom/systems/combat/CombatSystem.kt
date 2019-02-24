package com.jscisco.lom.systems.combat

import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.commands.AttackCommand
import com.jscisco.lom.commands.DestroyCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.events.CombatInstigatedEvent
import com.jscisco.lom.events.DamageEvent
import com.jscisco.lom.events.MissEvent
import com.jscisco.lom.events.OnHitEvent
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.publishEvent
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import java.lang.Integer.max

object CombatSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AttackCommand> { (context: GameContext, attacker, defender) ->
        //  From the attacker, get the total toHit
        //  From the defender, get the total EV (evasion

        val attack = Attack(attacker, defender,
                attackPower = attacker.attackPower, toHit = attacker.toHit,
                ac = defender.ac, ev = defender.ev)

        attacker.publishEvent(CombatInstigatedEvent(context, attacker, defender, attack))
        defender.publishEvent(CombatInstigatedEvent(context, attacker, defender, attack))

        if (hitSuccessful(attack)) {
            attacker.publishEvent(OnHitEvent(context, attacker, defender))
            defender.publishEvent(OnHitEvent(context, attacker, defender))
            val damage = calculateDamage(attack)
            defender.health.hpProperty.value -= damage
            attacker.publishEvent(DamageEvent(context, attacker, defender, damage))
            defender.publishEvent(DamageEvent(context, attacker, defender, damage))

        } else {
            attacker.publishEvent(MissEvent(context, attacker, defender))
            defender.publishEvent(MissEvent(context, attacker, defender))
        }

        defender.health.whenShouldBeDestroyed {
            defender.executeCommand(DestroyCommand(context, defender, ""))
        }
        Consumed
    }

    private fun hitSuccessful(attack: Attack): Boolean {
        logger.debug("%s attacked %s and had %s toHit vs their %s ev".format(attack.attacker, attack.defender, attack.toHit, attack.ev))
        return (attack.toHit >= attack.ev)
    }

    private fun calculateDamage(attack: Attack): Int {
        val damage = max(1, attack.attackPower - attack.ac)
        logger.debug("%s dealt %s damage to %s.".format(attack.attacker, damage, attack.defender))
        return damage
    }
}