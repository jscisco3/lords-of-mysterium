package com.jscisco.lom.kingdom.buildings

import com.jscisco.lom.item.Item

class Workshop(val availableItems: List<Item>) : Building(name = "Workshop", description = "A clever Workshop", level = 1, cost = 200)