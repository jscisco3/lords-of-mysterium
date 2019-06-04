package com.jscisco.lom.extensions

import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.EntityType

inline fun <reified T : Command<out EntityType, GameContext>> Command<out EntityType, GameContext>.responseWhenCommandIs(
        noinline fn: (T) -> Response): Response {
    return responseWhenCommandIs(T::class, fn)
}
