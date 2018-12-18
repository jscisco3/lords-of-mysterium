package com.jscisco.lom.dungeon

import com.jscisco.lom.entities.Entity
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.api.screen.Screen

data class Context(val dungeon: Dungeon,
                   val screen: Screen,
                   val input: Input,
                   val entity: Entity,
                   val entityPosition: Maybe<Position3D>,
                   val player: Entity
) {

    fun whenHasPosition(fn: (Position3D) -> Unit) {
        entityPosition.map(fn)
    }
}