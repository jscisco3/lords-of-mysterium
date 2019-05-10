package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.builders.GameTileBuilder
import org.hexworks.zircon.api.data.impl.Position3D

class Monster : Actor(tile = GameTileBuilder.GOBLIN, position = Position3D.unknown()) {
    override val fieldOfView: FieldOfView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}