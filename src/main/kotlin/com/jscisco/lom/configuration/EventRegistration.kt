package com.jscisco.lom.configuration

import com.jscisco.lom.events.DamageEvent
import com.jscisco.lom.events.GameLogEvent
import com.jscisco.lom.events.OnHitEvent
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.internal.Zircon

object EventRegistration {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun registerEvents() {
        registerDamageEvent()
        registerOnHitEvent()
    }

    private fun registerDamageEvent() {
        Zircon.eventBus.subscribe<DamageEvent> { (source, target, amount) ->
            // target.hp -= amount
            logger.info("%s dealt %s damage to %s".format(source, target, amount))
            Zircon.eventBus.publish(GameLogEvent("%s damage dealt to %s by %s".format(amount, target, source)))
        }
    }

    private fun registerOnHitEvent() {
        Zircon.eventBus.subscribe<OnHitEvent> { (source, target) ->
            logger.info("%s hit %s!".format(source, target))
            Zircon.eventBus.publish(DamageEvent(target, source, 10))
        }
    }
}