package com.jscisco.lom.extensions

import com.jscisco.lom.entities.Entity
import com.jscisco.lom.entities.attributes.Attribute
import com.jscisco.lom.entities.attributes.flags.BlockOccupier
import com.jscisco.lom.entities.attributes.flags.Player
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile

inline fun <reified T : Attribute> Entity.attribute(): T = attribute(T::class.java).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${T::class.simpleName}'.")
}

inline fun <reified T : Attribute> Entity.hasAttribute() = attribute(T::class.java).isPresent

inline fun <reified T : Attribute> Entity.whenHasAttribute(crossinline fn: (T) -> Unit) = attribute(T::class.java).map(fn)


val Entity.tile: Tile
    get() = metadata.tile

val Entity.occupiesBlock: Boolean
    get() = hasAttribute<BlockOccupier>()

val Entity.isPlayer: Boolean
    get() = hasAttribute<Player>()