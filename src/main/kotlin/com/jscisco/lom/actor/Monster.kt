package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.commands.Consumed
import com.jscisco.lom.commands.Response
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

class Monster(override val health: Health = Health(20)) : Actor(tile = GameTileBuilder.GOBLIN, name = "Goblin") {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)


    override val fieldOfView: FieldOfView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


    override fun takeTurn(): Response {
        logger.info("${name} is taking their turn!")
        this.initiative.cooldown += 1000
        return Consumed
    }
}