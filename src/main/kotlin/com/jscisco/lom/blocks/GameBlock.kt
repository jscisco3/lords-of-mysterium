package com.jscisco.lom.blocks

import GameEntity
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.blocksVision
import com.jscisco.lom.extensions.occupiesBlock
import com.jscisco.lom.extensions.tile
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileBuilder.FLOOR,
                private val currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf(),
                var seen: Boolean = false,
                var inFov: Boolean = false,
                var lastSeen: Tile = GameTileBuilder.FLOOR) : BlockBase<Tile>() {

    // We have decided that there are only two layers per block: defaultTile (floor),
    // or the last non-item, or the last item (if no non-item is present)
    override val layers: MutableList<Tile>
        get() {
//            if (inFov) {
//                return mutableListOf(terrain.tile, getEntityTile())
//            } else {
//                if (seen) {
//                    return mutableListOf(terrain.tile, lastSeen)
//                }
//            }
//            return mutableListOf(terrain.tile, terrain.tile)
            return mutableListOf(defaultTile, currentEntities.map {
                it.tile
            }.let {
                it.firstOrNull()
            } ?: GameTileBuilder.EMPTY)
        }

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    val entities: Iterable<GameEntity<EntityType>>
        get() = currentEntities.toList()

    val occupier: GameEntity<EntityType>
        get() = currentEntities.firstOrNull {
            it.occupiesBlock
        } ?: throw NoSuchElementException("This block is not occupied")

    val isOccupied: Boolean
        get() = currentEntities.any {
            it.occupiesBlock
        }

    val blocksVision: Boolean
        get() = currentEntities.any {
            it.blocksVision
        }

    fun addEntity(entity: GameEntity<EntityType>) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        currentEntities.remove(entity)
    }

    companion object {
        fun create(): GameBlock = GameBlock()

        fun createWith(entity: GameEntity<EntityType>): GameBlock = GameBlock().also {
            it.addEntity(entity)
        }
    }
}