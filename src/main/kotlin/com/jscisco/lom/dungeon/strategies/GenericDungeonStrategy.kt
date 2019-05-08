package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidgrid.mapping.SerpentMapGenerator
import squidpony.squidmath.StatefulRNG

class GenericDungeonStrategy(private val dungeonSize: Size3D) : GenerationStrategy(dungeonSize) {
    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        val dungeonGenerator = DungeonGenerator(dungeonSize.xLength, dungeonSize.yLength)
        val rng = StatefulRNG()
        for (z in 0 until dungeonSize.zLength) {
            dungeonGenerator.addDoors(0, false)
            val serpent = SerpentMapGenerator(dungeonSize.xLength, dungeonSize.yLength, rng, 0.2)
            serpent.putWalledBoxRoomCarvers(2)
            serpent.putWalledRoundRoomCarvers(2)
            serpent.putCaveCarvers(2)
            val map = serpent.generate()
            DungeonUtility.closeDoors(dungeonGenerator.generate(map))
            dungeonGenerator.dungeon.forEachIndexed { row, arr ->
                arr.forEachIndexed { column, terrain ->
                    val position = Position3D.create(row, column, z)
                    if (terrain == '#') {
                        blocks[position] = GameBlockFactory.wall()
                    }
                    if (terrain == '.') {
                        blocks[position] = GameBlockFactory.floor()
                    }
//                    if (terrain == '+') {
//                        blocks[position] = GameBlockFactory.closedDoor()
//                    }
//                    if (terrain == '/') {
//                        blocks[position] = GameBlockFactory.openDoor()
//                    }
                }
            }
//            if (z > 0) {
//                val stairsDown = dungeonGenerator.stairsDown
//                blocks[Position3D.create(stairsDown.x, stairsDown.y, z)] = GameBlockFactory.stairsDown()
//            }
//            if (z < dungeonSize.zLength) {
//                val stairsUp = dungeonGenerator.stairsUp
//                blocks[Position3D.create(stairsUp.x, stairsUp.y, z)] = GameBlockFactory.stairsUp()
//            }
        }
        initializeOutsideWalls()
        return blocks
    }
}