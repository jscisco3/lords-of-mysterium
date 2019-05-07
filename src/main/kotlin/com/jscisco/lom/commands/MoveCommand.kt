package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D

class MoveCommand(dungeon: Dungeon, receiver: Actor, private val direction: Position3D) : Command(dungeon, receiver) {
    override fun invoke() {
        val oldPosition = receiver.position
        val newPosition = oldPosition.plus(direction)

        dungeon.fetchBlockAt(newPosition).ifPresent {
            if (!it.isOccupied) {
                receiver.position = newPosition
            }
        }
    }
}