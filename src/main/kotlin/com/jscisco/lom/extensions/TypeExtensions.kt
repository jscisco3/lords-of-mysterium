package com.jscisco.lom.extensions

import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Entity
import org.hexworks.amethyst.api.EntityType

/**
 * Fits any [Entity] type we use.
 */
typealias AnyGameEntity = Entity<EntityType, GameContext>

/**
 * Specializes [Entity] with our [GameContext] type.
 */
typealias GameEntity<T> = Entity<T, GameContext>