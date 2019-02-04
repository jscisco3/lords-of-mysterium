package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.dungeon.strategies.GenerationStrategy
import com.jscisco.lom.dungeon.strategies.GenericDungeonStrategy
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DungeonBuilder(private val dungeonSize: Size3D,
                     private val player: GameEntity<Player>,
                     private val strategy: GenerationStrategy = GenericDungeonStrategy(dungeonSize)) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val blocks: MutableMap<Position3D, GameBlock> = strategy.generateDungeon()

    fun build(visibleSize: Size3D, dungeonSize: Size3D) = Dungeon(blocks, visibleSize, dungeonSize, player)
}