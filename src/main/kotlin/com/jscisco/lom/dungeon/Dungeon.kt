package com.jscisco.lom.dungeon

import AnyGameEntity
import GameEntity
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.events.DoorOpenedEvent
import com.jscisco.lom.events.UpdateCamera
import com.jscisco.lom.events.UpdateFOW
import com.jscisco.lom.extensions.isPlayer
import com.jscisco.lom.extensions.position
import org.hexworks.amethyst.api.Engines.newEngine
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.Maybe
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
    //    private val fogOfWar: FogOfWar by lazy { FogOfWar(this) }
    val entities: MutableList<GameEntity<EntityType>> = mutableListOf()

    val player: GameEntity<Player>
        get() {
            return entities.last {
                it.isPlayer
            } as GameEntity<Player>
        }

    private val engine = newEngine<GameContext>()
    private val entityPositionLookup = mutableMapOf<Identifier, Position3D>()

    init {
        blocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.entities.forEach {
                entityPositionLookup[it.id] = pos
                engine.addEntity(it)
                entities.add(it)
                it.position = pos
            }
        }

        registerEvents()
//        fogOfWar.updateFOW()
        try {
            updateCamera()
        } catch (e: NoSuchElementException) {

        }
    }


    private fun registerEvents() {

        Zircon.eventBus.subscribe<DoorOpenedEvent> {
            //            fogOfWar.updateFOW()
        }

        Zircon.eventBus.subscribe<UpdateFOW> {
            //            fogOfWar.updateFOW()
        }

        Zircon.eventBus.subscribe<UpdateCamera> {
            this.updateCamera()
        }
    }

    fun updateCamera() {
        val screenPosition = player.position - visibleOffset()
        val halfHeight = visibleSize.yLength / 2
        val halfWidth = visibleSize.xLength / 2
        logger.trace("visible offset is: %s, screen position is: %s, half Height: %s, half width: %s".format(visibleOffset().toString(), screenPosition.toString(), halfHeight, halfWidth))
        if (screenPosition.y > halfHeight) {
            logger.debug("Scrolling forward by %s".format(screenPosition.y - halfHeight))
            scrollForwardBy(screenPosition.y - halfHeight)
        }
        if (screenPosition.y < halfHeight) {
            logger.debug("Scrolling backwards by %s".format(halfHeight - screenPosition.y))
            scrollBackwardBy(halfHeight - screenPosition.y)
        }
        if (screenPosition.x > halfWidth) {
            logger.debug("Scrolling right by %s".format(screenPosition.x - halfWidth))
            scrollRightBy(screenPosition.x - halfWidth)
        }
        if (screenPosition.x < halfWidth) {
            logger.debug("Scrolling left by %s".format(halfWidth - screenPosition.x))
            scrollLeftBy(halfWidth - screenPosition.x)
        }
    }

    /**
     * Finds an empty location on a given Z-level
     */
    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            val pos = Positions.create3DPosition(
                    x = (Math.random() * size.xLength).toInt() + offset.x,
                    y = (Math.random() * size.yLength).toInt() + offset.y,
                    z = offset.z)
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
     * Find an empty location on a given Z-Level
     */
    fun findEmptyLocationOnZLevel(zlevel: Int): Maybe<Position3D> {
        return findEmptyLocationWithin(Position3D.defaultPosition().withZ(zlevel), actualSize)
    }

    /**
     * Add an [Entity] at a given [Position3D]
     */
    fun addEntity(entity: Entity<EntityType, GameContext>, position: Position3D) {
//        engine.addEntity(entity)
        entities.add(entity)
        entity.position = position
        entityPositionLookup[entity.id] = position
        fetchBlockAt(position).map {
            it.addEntity(entity)
        }
    }

    fun removeEntity(entity: Entity<EntityType, GameContext>) {
        engine.removeEntity(entity)
        entities.remove(entity)
        entity.position = Position3D.unknown()
        entityPositionLookup.remove(entity.id)?.let { pos ->
            fetchBlockAt(pos).map {
                it.removeEntity(entity)
            }
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
                entity.position = position
                true
            } ?: false
        } else {
            false
        }
    }

    fun getEntitiesOnZLevel(zlevel: Int): List<AnyGameEntity> {
        return entities.filter {
            it.position.z == zlevel
        }
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}
