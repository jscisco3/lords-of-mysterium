package com.jscisco.lom.blocks

import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.entities.Entity
import com.jscisco.lom.extensions.tile
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileBuilder.floor(),
                private var currentEntities: MutableList<Entity> = mutableListOf()) : BlockBase<Tile>() {

    override val layers: MutableList<Tile>
        get() = mutableListOf(defaultTile, currentEntities.map { it.tile }.lastOrNull() ?: GameTileBuilder.EMPTY)

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    fun addEntity(entity: Entity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: Entity) {
        currentEntities.remove(entity)
    }

    companion object {
        fun create(): GameBlock = GameBlock()

        fun createWith(entity: Entity): GameBlock = GameBlock().also {
            it.addEntity(entity)
        }
    }
}