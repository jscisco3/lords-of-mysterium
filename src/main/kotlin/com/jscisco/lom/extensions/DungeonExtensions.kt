package com.jscisco.lom.extensions

import GameEntity
import com.jscisco.lom.attributes.FieldOfView
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius

fun Dungeon.addAtEmptyPosition(entity: GameEntity<EntityType>,
                               offset: Position3D = Positions.default3DPosition(),
                               size: Size3D = actualSize()): Boolean {
    return findEmptyLocationWithin(offset, size).fold(
            whenEmpty = {
                false
            },
            whenPresent = { location ->
                addEntity(entity, location)
                true
            }
    )
}

fun Dungeon.calculateResistanceMap(entity: GameEntity<EntityType>): Array<DoubleArray> {
    val calculatedResistanceMap: Array<DoubleArray> = Array(this.actualSize().xLength, { DoubleArray(this.actualSize().yLength) })
    for (x in 0 until this.actualSize().xLength) {
        for (y in 0 until this.actualSize().yLength) {
            this.fetchBlockAt(Position3D.create(x, y, entity.position.z)).ifPresent {
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

fun Dungeon.calculateFOV(entity: GameEntity<EntityType>) {
    val resistanceMap = this.calculateResistanceMap(entity)
    val fieldOfView: Maybe<FieldOfView> = entity.findAttribute(FieldOfView::class)
    fieldOfView.ifPresent {
        it.fov = FOV().calculateFOV(resistanceMap, entity.position.x, entity.position.y, it.radius, Radius.DIAMOND)
    }
    updateGameBlocks(entity)
}

fun Dungeon.updateGameBlocks(entity: GameEntity<EntityType>) {
    entity.findAttribute(FieldOfView::class).ifPresent {
        it.fov.forEachIndexed { x, doubles ->
            doubles.forEachIndexed { y, value ->
                fetchBlockAt(Position3D.create(x, y, entity.position.z)).ifPresent {
                    if (value > 0.0) {
                        it.seen = true
                        it.inFov = true
                        it.lastSeen = it.layers.last()
                    } else {
                        it.inFov = false
                    }
                }
            }
        }
    }
}