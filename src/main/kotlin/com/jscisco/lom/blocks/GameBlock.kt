package com.jscisco.lom.blocks

import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileBuilder.floor(),
                private var currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf()) : BlockBase<Tile>() {

    override val layers: MutableList<Tile>
        get() = mutableListOf(defaultTile, currentEntities.map { it.tile }.lastOrNull()
                ?: GameTileBuilder.EMPTY)

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    val isOccupied: Boolean
        get() = currentEntities.any {
            it.occupiesBlock
        }

    val isWall: Boolean
        get() = currentEntities.any {
            it.isWall
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