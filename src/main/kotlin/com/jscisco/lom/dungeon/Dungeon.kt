package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_HEIGHT
import com.jscisco.lom.configuration.GameConfiguration.WINDOW_WIDTH
import com.jscisco.lom.entities.Entity
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class Dungeon(private val visibleSize: Size3D, val maxDepth: Int) : GameArea<Tile, GameBlock> by GameAreaBuilder<Tile, GameBlock>()
        .withLayersPerBlock(2)
        .withVisibleSize(visibleSize)
        .withActualSize(Size3D.create(WINDOW_WIDTH, WINDOW_HEIGHT, 2))
        .withDefaultBlock(DEFAULT_BLOCK)
        .build() {

    private val entities = linkedMapOf<Identifier, Entity>()
    private val entityPositionLookup = mutableMapOf<Identifier, Position3D>()

    /**
     * Add a global entity, that is, an entity without a position
     */
    fun addDungeonEntity(entity: Entity) {
        entities[entity.id] = entity
    }

    /**
     * Add an [Entity] at a given [Position3D]
     * No effect if the [Entity] already exists in the dungeon
     */
    fun addEntity(entity: Entity, position: Position3D) {
        entities[entity.id] = entity
        if (entityPositionLookup.containsKey(entity.id).not()) {
            entityPositionLookup[entity.id] = position
            fetchBlockAt(position).map {
                it.addEntity(entity)
            }
        }
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}