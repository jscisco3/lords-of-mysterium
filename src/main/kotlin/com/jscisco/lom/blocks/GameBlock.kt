package com.jscisco.lom.blocks

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.item.Item
import com.jscisco.lom.terrain.Floor
import com.jscisco.lom.terrain.Terrain
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(var terrain: Terrain = Floor(),
                private var currentItems: MutableList<Item> = mutableListOf(),
                var actor: Maybe<Actor> = Maybe.ofNullable(null),
                var seen: Boolean = false,
                var inFov: Boolean = false,
                var lastSeen: Tile = GameTileBuilder.floor()) : BlockBase<Tile>() {

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
            return mutableListOf(terrain.tile, getEntityTile())
        }

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileBuilder.EMPTY
    }

    fun getEntityTile(): Tile {
        val itemTile: Tile? = currentItems.lastOrNull()?.tile
        val nonItemTile: Tile? = actor.fold(
                whenEmpty = { null },
                whenPresent = {
                    it.tile
                }
        )
        if (nonItemTile != null) {
            return nonItemTile
        }
        if (itemTile != null) {
            return itemTile
        }
        return terrain.tile
    }

    val isOccupied: Boolean
        get() = actor.isPresent

    val isWalkable: Boolean
        get() = terrain.walkable

    val blocksVision: Boolean
        get() = !terrain.transparent

    fun addActor(actor: Actor) {
        this.actor = Maybe.of(actor)
    }

    fun removeActor() {
        this.actor = Maybe.ofNullable(null)
    }

    val items: List<Item>
        get() = currentItems.toList()

    companion object {
        fun create(): GameBlock = GameBlock()

        fun createWith(actor: Actor): GameBlock = GameBlock().also {
            it.addActor(actor)
        }
    }
}