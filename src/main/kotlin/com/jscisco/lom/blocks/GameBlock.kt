package com.jscisco.lom.blocks

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.terrain.Floor
import com.jscisco.lom.terrain.Terrain
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var terrain: Terrain = Floor(),
                private var currentActors: MutableList<Actor> = mutableListOf(),
                var seen: Boolean = false,
                var inFov: Boolean = false,
                var lastSeen: Tile = GameTileBuilder.floor()) : BlockBase<Tile>() {

    // We have decided that there are only two layers per block: defaultTile (floor),
    // or the last non-item, or the last item (if no non-item is present)
    override val layers: MutableList<Tile>
        get() {
            if (inFov) {
                return mutableListOf(terrain.tile, getEntityTile())
            } else {
                if (seen) {
                    return mutableListOf(terrain.tile, lastSeen)
                }
            }
            return mutableListOf(terrain.tile, terrain.tile)
        }

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    fun getEntityTile(): Tile {
        val itemTile: Tile? = currentActors.filter { it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
        val nonItemTile: Tile? = currentActors.filter { !it.hasAttribute<Item>() }.map { it.tile }.lastOrNull()
        if (nonItemTile != null) {
            return nonItemTile
        }
        if (itemTile != null) {
            return itemTile
        }
        return GameTileBuilder.floor()
    }

    val isWall: Boolean
        get() = !terrain.walkable;

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