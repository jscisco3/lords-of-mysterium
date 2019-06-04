package com.jscisco.lom.dungeon

import GameEntity
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.dungeon.strategies.GenerationStrategy
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.addAtEmptyPosition
import com.jscisco.lom.extensions.position
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D,
                     private val player: GameEntity<Player>,
                     private val strategy: GenerationStrategy = GenericDungeonStrategy(dungeonSize)) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val blocks: MutableMap<Position3D, GameBlock> = strategy.generateDungeon()

    fun build(visibleSize: Size3D, dungeonSize: Size3D): Dungeon {
        val dungeon = Dungeon(blocks, visibleSize, dungeonSize, player)


        var playerStartPosition = dungeon.findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), dungeonSize)
        while (playerStartPosition.isEmpty()) {
            playerStartPosition = dungeon.findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), dungeonSize)
        }

        dungeon.addEntity(player, playerStartPosition.get())
        player.position = playerStartPosition.get()

        addNPCs(dungeon,
                Position3D.defaultPosition().withZ(0))
        return dungeon
    }

    private fun addNPCs(dungeon: Dungeon, offset: Position3D) {
        for (i in 0 until 5) {
            dungeon.addAtEmptyPosition(EntityFactory.newMonster(),
                    offset = offset)
        }
    }
}