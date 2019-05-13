package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.events.UpdateFOW
import com.jscisco.lom.terrain.ClosedDoor
import com.jscisco.lom.terrain.OpenDoor
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.internal.Zircon

class OpenDoorCommand(dungeon: Dungeon, receiver: Actor, private val position: Position3D) : Command(dungeon, receiver) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(): Response {
        var response: Response = Pass
        dungeon.fetchBlockAt(position).ifPresent {
            if (it.terrain is ClosedDoor) {
                it.terrain = OpenDoor()
                Zircon.eventBus.publish(UpdateFOW())

                response = Consumed
                return@ifPresent
            }
        }
        return response
    }
}