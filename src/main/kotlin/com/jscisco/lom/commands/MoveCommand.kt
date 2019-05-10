package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.extensions.calculateFOV
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D

class MoveCommand(dungeon: Dungeon, receiver: Actor, private val newPosition: Position3D) : Command(dungeon, receiver) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(): Response {
        logger.info("Moving to ${newPosition}")
        dungeon.fetchBlockAt(newPosition).ifPresent {
            if (!it.isOccupied && it.isWalkable) {
                // This should probably be changed
                dungeon.moveEntity(receiver, newPosition)
                dungeon.calculateFOV(receiver)
                Consumed
            }
        }
        return Pass
    }
}