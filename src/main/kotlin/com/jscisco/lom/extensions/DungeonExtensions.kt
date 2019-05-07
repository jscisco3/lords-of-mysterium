package com.jscisco.lom.extensions

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

fun Dungeon.addAtEmptyPosition(actor: Actor,
                               offset: Position3D = Positions.default3DPosition(),
                               size: Size3D = actualSize()): Boolean {
    return findEmptyLocationWithin(offset, size).fold(
            whenEmpty = {
                false
            },
            whenPresent = { location ->
                addActor(actor, location)
            }
    )
}