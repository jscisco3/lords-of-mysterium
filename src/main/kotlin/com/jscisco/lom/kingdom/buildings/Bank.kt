package com.jscisco.lom.kingdom.buildings

class Bank : Building(name = "Bank", description = "A skeevy Bank", level = 1, cost = 1000) {
    val maximumGoldBonus: Int
        get() = 1000 * level
}