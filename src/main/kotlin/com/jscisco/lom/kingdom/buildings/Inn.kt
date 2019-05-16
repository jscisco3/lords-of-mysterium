package com.jscisco.lom.kingdom.buildings

class Inn : Building(name = "Inn", description = "A reputable Inn", level = 1, cost = 500) {
    val maxHeroes: Int
        get() = level * 3
}