package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.GameLogEvent
import org.hexworks.zircon.internal.Zircon

class AttackCommand(dungeon: Dungeon, private val attacker: Actor, private val defender: Actor) : Command(dungeon, attacker) {

    override fun invoke(): Response {
        defender.health.hp -= 5
        Zircon.eventBus.publish(GameLogEvent("${attacker.name} attacked the ${defender.name}"))
        return Consumed
    }
}