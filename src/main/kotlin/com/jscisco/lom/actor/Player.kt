package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.commands.Consumed
import com.jscisco.lom.commands.Response
import com.jscisco.lom.events.StartPlayerTurn
import org.hexworks.zircon.internal.Zircon

class Player : Actor(tile = GameTileBuilder.PLAYER, name = "Hero") {
    override val fieldOfView: FieldOfView = FieldOfView(10.0)
    val inventory: Inventory = Inventory()

    override val health: Health = Health(100)

    override fun takeTurn(): Response {
        Zircon.eventBus.publish(StartPlayerTurn())
        return Consumed
    }
}