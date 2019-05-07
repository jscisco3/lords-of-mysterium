package com.jscisco.lom.kingdom

import com.jscisco.lom.actor.Actor

class Kingdom(val name: String) {
    val heroes: List<Actor> = emptyList()
    val upgrades: List<Upgrade> = emptyList()

}