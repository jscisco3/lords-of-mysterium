package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.attributes.Health
import com.jscisco.lom.builders.GameTileBuilder

class Monster(override val health: Health = Health(20)) : Actor(tile = GameTileBuilder.GOBLIN, name = "Goblin") {
    override val fieldOfView: FieldOfView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

}