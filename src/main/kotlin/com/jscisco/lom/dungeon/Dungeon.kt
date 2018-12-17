package com.jscisco.lom.dungeon

class Dungeon(val maxDepth: Int) {

    val dungeonFloors: MutableList<DungeonFloor> = mutableListOf()

    init {
        for (i in 0..maxDepth) {
            dungeonFloors.add(i, DungeonFloor(10, 10))
        }
    }
}