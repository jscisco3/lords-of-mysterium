package com.jscisco.lom.blocks

import com.jscisco.lom.builders.GameTileRepository
import com.jscisco.lom.entities.Entity
import com.jscisco.lom.extensions.tile
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileRepository.floor(),
                private val currentEntities: MutableList<Entity> = mutableListOf()) : BlockBase<Tile>() {

    override val layers: MutableList<Tile>
        get() = mutableListOf(defaultTile, currentEntities.map { it.tile }.lastOrNull() ?: GameTileRepository.EMPTY)

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileRepository.EMPTY
    }

    val entities: Iterable<Entity>
        get() = currentEntities.toList()

    fun addEntity(entity: Entity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: Entity) {
        currentEntities.remove(entity)
    }

    companion object {
        fun create(): GameBlock = GameBlock()

        fun createWith(entity: Entity) = GameBlock().also {
            it.addEntity(entity)
        }

    }
}