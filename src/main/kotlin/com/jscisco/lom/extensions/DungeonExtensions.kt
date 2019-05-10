package com.jscisco.lom.extensions

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius

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

fun Dungeon.calculateResistanceMap(actor: Actor): Array<DoubleArray> {
    val calculatedResistanceMap: Array<DoubleArray> = Array(this.actualSize().xLength, { DoubleArray(this.actualSize().yLength) })
    for (x in 0 until this.actualSize().xLength) {
        for (y in 0 until this.actualSize().yLength) {
            this.fetchBlockAt(Position3D.create(x, y, actor.position.z)).ifPresent {
                if (it.blocksVision) {
                    calculatedResistanceMap[x][y] = 1.0
                } else {
                    calculatedResistanceMap[x][y] = 0.0
                }
            }

        }
    }
    return calculatedResistanceMap
}

fun Dungeon.calculateFOV(actor: Actor) {
    val resistanceMap = this.calculateResistanceMap(actor)
    actor.fieldOfView.fov = FOV().let {
        it.calculateFOV(resistanceMap, actor.position.x, actor.position.y, actor.fieldOfView.radius, Radius.DIAMOND)
    }
}