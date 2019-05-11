package com.jscisco.lom.actor

import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.builders.GameTileBuilder

class Monster : Actor(tile = GameTileBuilder.GOBLIN) {
    override val fieldOfView: FieldOfView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}