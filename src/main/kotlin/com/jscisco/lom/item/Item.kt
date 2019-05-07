package com.jscisco.lom.item

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

abstract class Item(val tile: Tile) {
    protected abstract val positionProperty: Property<Position3D>
    var position: Position3D by positionProperty.asDelegate()
}