package com.jscisco.lom.blocks

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.StairsDown
import com.jscisco.lom.attributes.types.StairsUp
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileBuilder.floor(),
                private var currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf(),
                var seen: Boolean = false,
                var inFov: Boolean = false,
                var lastSeen: Tile = GameTileBuilder.floor()) : BlockBase<Tile>() {

    // We have decided that there are only two layers per block: defaultTile (floor),
    // or the last non-item, or the last item (if no non-item is present)
    override val layers: MutableList<Tile>
        get() {
            if (inFov) {
                return mutableListOf(defaultTile, getEntityTile())
            } else {
                if (seen) {
                    return mutableListOf(defaultTile, lastSeen)
                }
            }
            return mutableListOf(defaultTile, defaultTile)
        }

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    fun getEntityTile(): Tile {
        val itemTile: Tile? = currentEntities.filter { it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
        val nonItemTile: Tile? = currentEntities.filter { !it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
        if (nonItemTile != null) {
            return nonItemTile
        }
        if (itemTile != null) {
            return itemTile
        }
        return GameTileBuilder.floor()
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

    val isClosedDoor: Boolean
        get() = currentEntities.any {
            it.isClosedDoor
        }

    val isStairsDown: Boolean
        get() = currentEntities.any {
            it.type is StairsDown
        }

    val isStairsUp: Boolean
        get() = currentEntities.any {
            it.type is StairsUp
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