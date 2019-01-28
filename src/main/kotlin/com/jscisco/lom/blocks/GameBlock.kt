package com.jscisco.lom.blocks

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileBuilder.floor(),
                private var currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf()) : BlockBase<Tile>() {

    // We have decided that there are only two layers per block: defaultTile (floor),
    // or the last non-item, or the last item (if no non-item is present)
    override val layers: MutableList<Tile>
        get() = mutableListOf(defaultTile, currentEntities.filter { !it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
                ?: currentEntities.filter { it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
                ?: GameTileBuilder.EMPTY)

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    val occupier: GameEntity<EntityType>
        @Throws(NoSuchElementException::class)
        get() = currentEntities.firstOrNull { it.occupiesBlock }
                ?: throw NoSuchElementException("This block is no occupied!")


    val isOccupied: Boolean
        get() = currentEntities.any {
            it.occupiesBlock
        }

    val isWall: Boolean
        get() = currentEntities.any {
            it.isWall
        }

    val blocksVision: Boolean
        get() = currentEntities.any {
            it.blocksVision
        }

    val entities: Iterable<GameEntity<EntityType>>
        get() = currentEntities.toList()

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