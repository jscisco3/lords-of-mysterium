package com.jscisco.lom.kingdom

import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

class Kingdom(val name: String) {
    val heroes: List<GameEntity<EntityType>> = emptyList()
    val upgrades: List<Upgrade> = emptyList()

}