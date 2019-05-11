package com.jscisco.lom.item

import com.jscisco.lom.capabilities.Attack
import com.jscisco.lom.capabilities.Defense
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

class Item(val tile: Tile) {
    var position: Position3D = Position3D.defaultPosition()

    var quantity: Int = 1

    var attack: Maybe<Attack> = Maybe.ofNullable(null)
    var defense: Maybe<Defense> = Maybe.ofNullable(null)
}