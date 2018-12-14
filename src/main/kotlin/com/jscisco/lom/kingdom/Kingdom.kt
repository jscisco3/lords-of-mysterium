package com.jscisco.lom.kingdom

import com.jscisco.lom.entities.Entity

class Kingdom(val name: String) {
    val heroes: List<Entity> = emptyList()
    val upgrades: List<Upgrade> = emptyList()

}