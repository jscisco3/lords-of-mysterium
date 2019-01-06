package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.events.MoveEntity
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.filterType
import org.hexworks.amethyst.api.Engines.newEngine
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.internal.Zircon


class Dungeon(private val blocks: MutableMap<Position3D, GameBlock>,
              private val visibleSize: Size3D,
              private val actualSize: Size3D) : GameArea<Tile, GameBlock> by GameAreaBuilder<Tile, GameBlock>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .withDefaultBlock(DEFAULT_BLOCK)
        .withLayersPerBlock(2)
        .build() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val engine = newEngine<GameContext>()
    private val entityPositionLookup = mutableMapOf<Identifier, Position3D>()

    init {
        blocks.forEach { pos, block ->
            setBlockAt(pos, block)
            block.entities.forEach {
                entityPositionLookup[it.id] = pos
            }
        }

        Zircon.eventBus.subscribe<MoveEntity> { (entity, position) ->
            moveEntity(entity, position)
        }

//        updateCamera()

    }

//    fun handleInput(input: Input) {
//        input.whenKeyStroke { ks ->
//            val newPos = when (ks.getCharacter()) {
//                'w' -> entityPositionLookup[hero.id]!!.withRelativeY(-1)
//                's' -> entityPositionLookup[hero.id]!!.withRelativeY(1)
//                'a' -> entityPositionLookup[hero.id]!!.withRelativeX(-1)
//                'd' -> entityPositionLookup[hero.id]!!.withRelativeX(1)
////                ',' -> hero.executeCommand(PickItemUp(context = GameContext(), source = hero, position = entityPositionLookup[hero.id]!!))
//                else -> {
//                    entityPositionLookup[hero.id]!!
//                }
//            }
//            if (fetchBlockAt(newPos).get().isOccupied.not()) {
//                Zircon.eventBus.publish(MoveEntity(hero, newPos))
//            }
//        }
//        // Update camera position to be ~centered~ on the hero
//        updateCamera()
//    }

//    private fun updateCamera() {
//        val screenPosition = findPositionOf(hero).get() - visibleOffset()
//        val halfHeight = visibleSize.yLength / 2
//        val halfWidth = visibleSize.xLength / 2
//        if (screenPosition.y > halfHeight) {
//            scrollForwardBy(screenPosition.y - halfHeight)
//        }
//        if (screenPosition.y < halfHeight) {
//            scrollBackwardBy(halfHeight - screenPosition.y)
//        }
//        if (screenPosition.x > halfWidth) {
//            scrollRightBy(screenPosition.x - halfWidth)
//        }
//        if (screenPosition.x < halfWidth) {
//            scrollLeftBy(halfWidth - screenPosition.x)
//        }
//    }

    /**
     * Finds the [Position3D] of the given [Entity].
     */
    fun findPositionOf(entity: GameEntity<EntityType>): Maybe<Position3D> {
        return Maybe.ofNullable(entityPositionLookup[entity.id])
    }

    /**
     * Finds an empty location on a given Z-level
     */
    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        logger.info("Offset: %s | Size: %s".format(offset.toString(), size.toString()))
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            val pos = Positions.create3DPosition(
                    x = (Math.random() * size.xLength).toInt() + offset.x,
                    y = (Math.random() * size.yLength).toInt() + offset.y,
                    z = (Math.random() * size.zLength).toInt() + offset.z)
            fetchBlockAt(pos).map {
                if (it.isOccupied.not()) {
                    position = Maybe.of(pos)
                }
            }
            currentTry++
        }
        return position
    }

    /**
     * Add a global entity, that is, an entity without a position
     */
    fun addDungeonEntity(entity: Entity<EntityType, GameContext>) {
        engine.addEntity(entity)
    }

    /**
     * Add an [Entity] at a given [Position3D]
     * No effect if the [Entity] already exists in the dungeon
     */
    fun addEntity(entity: Entity<EntityType, GameContext>, position: Position3D) {
        engine.addEntity(entity)
        if (entityPositionLookup.containsKey(entity.id).not()) {
            entityPositionLookup[entity.id] = position
            fetchBlockAt(position).map {
                it.addEntity(entity)
            }
        }
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        engine.removeEntity(entity)
        entityPositionLookup.remove(entity.id)?.let { pos ->
            fetchBlockAt(pos).map { it.removeEntity(entity) }
        }
    }

    /**
     * Move an [Entity] to the desired [Postion3D]
     * @return true if the [Entity] was moved
     */
    fun moveEntity(entity: GameEntity<EntityType>, position: Position3D): Boolean {
        return if (actualSize().containsPosition(position) && position.x >= 0 && position.y >= 0) {
            entityPositionLookup.remove(entity.id)?.let { oldPos ->
                fetchBlockAt(oldPos).map { oldBlock ->
                    oldBlock.removeEntity(entity)
                }
                fetchBlockAt(position).map { newBlock ->
                    newBlock.addEntity(entity)
                }
                entityPositionLookup[entity.id] = position
                true
            } ?: false
        } else {
            false
        }
    }

    fun fetchEntitiesAt(pos: Position3D): List<GameEntity<EntityType>> {
        return fetchBlockAt(pos).fold(whenEmpty = { kotlin.collections.listOf() }, whenPresent = {
            it.entities.toList()
        })
    }

    fun findItemsAt(pos: Position3D): List<GameEntity<Item>> {
        return fetchEntitiesAt(pos).filterType()
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}
