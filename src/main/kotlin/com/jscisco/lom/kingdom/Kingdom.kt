package com.jscisco.lom.kingdom

import GameEntity
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.kingdom.buildings.Bank
import com.jscisco.lom.kingdom.buildings.Building
import com.jscisco.lom.kingdom.buildings.Inn

class Kingdom(val name: String, val initialMaximumGold: Int = 1000, val initialMaximumHeroes: Int = 2) {
    val availableHeroes: MutableList<GameEntity<Player>> = mutableListOf()
    val maximumHeroes: Int
        get() = initialMaximumHeroes + ((buildings.lastOrNull { it is Inn } as Inn?)?.maxHeroes ?: 0)

    val buildings: MutableList<Building> = mutableListOf()

    var currentGold: Int = 0
    val maximumGold: Int
        get() = initialMaximumGold + ((buildings.lastOrNull { it is Bank } as Bank?)?.maximumGoldBonus ?: 0)

    fun addGold(gold: Int) {
        currentGold += gold
        currentGold.coerceAtMost(maximumGold)
    }

    fun buildBuilding(building: Building) {
        if (buildingIsAvailableToBuild(building) && building.cost <= currentGold) {
            currentGold -= building.cost
            buildings.add(building)
        }
    }

    fun hireHero(hero: GameEntity<Player>, cost: Int) {
        if (availableHeroes.size < maximumHeroes && cost <= currentGold) {
            availableHeroes.add(hero)
            currentGold -= cost
        }
    }

    fun buildingIsAvailableToBuild(building: Building): Boolean {
        return buildings.none { it.name == building.name }
    }

}