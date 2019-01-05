package com.jscisco.lom.extensions

import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType

/**
 * Fits any [Entity] type we use.
 */
typealias AnyGameEntity = Entity<EntityType, GameContext>

/**
 * Specializes [Entity] with our [GameContext] type.
 */
typealias GameEntity<T> = Entity<T, GameContext>

/**
 * Specializes a [Command] with our [GameContext] type
 */
typealias GameCommand<T> = Command<T, GameContext>