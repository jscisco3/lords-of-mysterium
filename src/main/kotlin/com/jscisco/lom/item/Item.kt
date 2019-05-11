package com.jscisco.lom.item

import com.jscisco.lom.capabilities.Attack
import com.jscisco.lom.capabilities.Defense
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import java.util.*

class Item(val tile: Tile, initialPosition: Position3D = Position3D.unknown(), val name: String) {
    val id: UUID = UUID.randomUUID()

    private val positionProperty = createPropertyFrom(initialPosition)
    var position: Position3D by positionProperty.asDelegate()

    var quantity: Int = 1

    var attack: Maybe<Attack> = Maybe.ofNullable(null)
    var defense: Maybe<Defense> = Maybe.ofNullable(null)
}