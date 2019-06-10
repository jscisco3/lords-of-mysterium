package com.jscisco.lom.extensions

import AnyGameEntity
import com.jscisco.lom.attributes.EntityActions
import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile

inline fun <reified T : Attribute> AnyGameEntity.findAttribute(): T = findAttribute(T::class).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${T::class.simpleName}'.")
}

inline fun <reified T : Attribute> AnyGameEntity.hasAttribute() = findAttribute(T::class).isPresent

inline fun <reified T : Attribute> AnyGameEntity.whenHasAttribute(crossinline fn: (T) -> Unit) = findAttribute(T::class).map(fn)

fun AnyGameEntity.tryActionsOn(context: GameContext, target: AnyGameEntity): Response {
    findAttribute<EntityActions>()
            .createActionsFor(context, this, target)
            .forEach { action ->
                if (target.executeCommand(action) is Consumed) {
                    return Consumed
                }
            }
    return Pass
}

var AnyGameEntity.position
    get() = findAttribute(EntityPosition::class).orElseThrow {
        IllegalArgumentException("This Entity has no EntityPosition")
    }.position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.findAttribute<EntityTile>().tile

val AnyGameEntity.occupiesBlock: Boolean
    get() = hasAttribute<BlockOccupier>()

val AnyGameEntity.blocksVision: Boolean
    get() = hasAttribute<VisionBlocker>()

val AnyGameEntity.isPlayer: Boolean
    get() = this.type == Player