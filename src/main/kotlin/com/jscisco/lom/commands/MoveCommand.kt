package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.UpdateCamera
import com.jscisco.lom.events.UpdateFOW
import com.jscisco.lom.extensions.calculateFOV
import com.jscisco.lom.terrain.ClosedDoor
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.internal.Zircon

class MoveCommand(dungeon: Dungeon, receiver: Actor, private val newPosition: Position3D) : Command(dungeon, receiver) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(): Response {
        var response: Response = Pass
        dungeon.fetchBlockAt(newPosition).ifPresent {
            if (it.isOccupied) {
                response = AttackCommand(dungeon, receiver, it.actor.get()).invoke()
                return@ifPresent
            }
            if (it.isWalkable.not() && it.terrain is ClosedDoor) {
                response = OpenDoorCommand(dungeon, receiver, newPosition).invoke()
                return@ifPresent
            }
            if (it.isWalkable) {
                // This should probably be changed
                dungeon.moveEntity(receiver, newPosition)
                dungeon.calculateFOV(receiver)
                Zircon.eventBus.publish(UpdateFOW())
                Zircon.eventBus.publish(UpdateCamera())
                response = Consumed
                return@ifPresent
            }
        }
        return response
    }
}