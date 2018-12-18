package com.jscisco.lom.dungeon

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockRepository
import com.jscisco.lom.entities.Entity
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class Dungeon(startingBlocks: Map<Position3D, GameBlock>,
              visibleSize: Size3D,
              actualSize: Size3D) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .withDefaultBlock(DEFAULT_BLOCK)
        .withLayersPerBlock(2)
        .build() {

    private val entities = linkedMapOf<Identifier, Entity>()
    private val entityPositionLookup = mutableMapOf<Identifier, Position3D>()

    init {
        startingBlocks.forEach { pos, block ->
            setBlockAt(pos, block)
            block.entities.forEach {
                entityPositionLookup[it.id] = pos
            }
        }
    }

    /**
     * Add an entity without a position
     */
    fun addDungeonEntity(entity: Entity) {
        entities[entity.id] = entity
    }

    /**
     * Add the given [Entity] at the given [Position3D]
     * Has no effect if the given [Entity] already in the dungeon
     */
    fun addEntity(entity: Entity, position: Position3D) {
        entities[entity.id]
        if (entityPositionLookup.containsKey(entity.id).not()) {
            entityPositionLookup[entity.id] = position
            fetchBlockAt(position).map {
                it.addEntity(entity)
            }
        }
    }

    /**
     * Remove the given [Entity]
     */
    fun removeEntity(entity: Entity) {
        entities.remove(entity.id)
        entityPositionLookup.remove(entity.id)?.let { pos ->
            fetchBlockAt(pos).map {
                it.removeEntity(entity)
            }
        }
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockRepository.floor()
    }
}