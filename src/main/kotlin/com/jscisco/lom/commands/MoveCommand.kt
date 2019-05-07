package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D

class MoveCommand(dungeon: Dungeon, receiver: Actor, private val newPosition: Position3D) : Command(dungeon, receiver) {
    override fun invoke(): Response {
        dungeon.fetchBlockAt(newPosition).ifPresent {
            if (!it.isOccupied && it.isWalkable) {
                // This should probably be changed
                dungeon.moveEntity(receiver, newPosition)
                Consumed
            }
        }
        return Pass
    }
}