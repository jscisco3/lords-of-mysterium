package com.jscisco.lom.extensions

import com.jscisco.lom.attributes.EntityActions
import com.jscisco.lom.attributes.EntityPosition
import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.NameAttribute
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.Wall
import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import kotlin.reflect.full.isSuperclassOf

var AnyGameEntity.position: Position3D
    get() = attribute(EntityPosition::class).orElseThrow {
        IllegalArgumentException("This entity has no position!")
    }.position
    set(value) {
        attribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.attribute<EntityTile>().tile

val AnyGameEntity.occupiesBlock: Boolean
    get() = hasAttribute<BlockOccupier>()

val AnyGameEntity.blocksVision: Boolean
    get() = hasAttribute<VisionBlocker>()

val AnyGameEntity.isPlayer: Boolean
    get() = this.type == Player

val AnyGameEntity.isWall: Boolean
    get() = this.type == Wall

val AnyGameEntity.nameAttribute: NameAttribute
    get() = attribute()

inline fun <reified T : Attribute> AnyGameEntity.attribute(): T = attribute(T::class).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${T::class.simpleName}'.")
}

inline fun <reified T : Attribute> AnyGameEntity.hasAttribute() = attribute(T::class).isPresent

inline fun <reified T : Attribute> AnyGameEntity.whenHasAttribute(crossinline fn: (T) -> Unit) = attribute(T::class).map(fn)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : EntityType> Iterable<AnyGameEntity>.filterType(): List<Entity<T, GameContext>> {
    return filter { T::class.isSuperclassOf(it.type::class) }.toList() as List<Entity<T, GameContext>>
}


fun AnyGameEntity.tryActionsOn(context: GameContext, target: AnyGameEntity) {
    attribute<EntityActions>()
            .createActionsFor(context, this, target)
            .forEach { action ->
                if (target.executeCommand(action) is Consumed) {
                    return@forEach
                }
            }
}