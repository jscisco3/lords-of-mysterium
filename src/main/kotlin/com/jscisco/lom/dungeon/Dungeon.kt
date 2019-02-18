package com.jscisco.lom.dungeon

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.commands.*
import com.jscisco.lom.entities.FogOfWar
import com.jscisco.lom.events.DoorOpenedEvent
import com.jscisco.lom.events.EntityMovedEvent
import com.jscisco.lom.extensions.*
import com.jscisco.lom.view.dialog.EquipmentDialog
import com.jscisco.lom.view.dialog.InventoryDialog
import org.hexworks.amethyst.api.Engines.newEngine
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
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
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.api.input.InputType
import org.hexworks.zircon.api.kotlin.whenKeyStroke
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.Zircon
import java.io.File
import java.util.concurrent.ConcurrentHashMap


class Dungeon(private val blocks: MutableMap<Position3D, GameBlock>,
              private val visibleSize: Size3D,
              private val actualSize: Size3D,
              val player: GameEntity<Player>) : GameArea<Tile, GameBlock> by GameAreaBuilder<Tile, GameBlock>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .withDefaultBlock(DEFAULT_BLOCK)
        .withLayersPerBlock(2)
        .build() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val engine = newEngine<GameContext>()
    private val entityPositionLookup = mutableMapOf<Identifier, Position3D>()
    private val fogOfWar: FogOfWar by lazy { EntityFactory.newFogOfWar(this, player, actualSize) }

    val resistanceMap = ConcurrentHashMap<Int, Array<DoubleArray>>().also {
        for (z in 0 until actualSize.zLength) {
            it[z] = Array(actualSize.xLength, { DoubleArray(actualSize.yLength) })
        }
    }

    init {
        blocks.forEach { pos, block ->
            setBlockAt(pos, block)
            block.entities.forEach {
                entityPositionLookup[it.id] = pos
                engine.addEntity(it)
                it.position = pos
            }
        }

        registerEvents()
        calculateResistanceMap(resistanceMap)

        var playerStartPosition = findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), actualSize)
        while (playerStartPosition.isEmpty()) {
            playerStartPosition = findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), actualSize)
        }

        addEntity(player, playerStartPosition.get())
        logger.info("The player is at: %s".format(player.position))
        addDungeonEntity(fogOfWar)
        updateCamera()
    }


    private fun registerEvents() {
        Zircon.eventBus.subscribe<EntityMovedEvent> { (entity, _) ->
            if (entity.isPlayer) {
                updateCamera()
            }
        }

        Zircon.eventBus.subscribe<DoorOpenedEvent>() {
            calculateResistanceMap(resistanceMap)
        }
    }

    fun calculateResistanceMap(resistanceMap: MutableMap<Int, Array<DoubleArray>>) {
        blocks.forEach { pos, block ->
            if (block.blocksVision) {
                resistanceMap.getValue(pos.z)[pos.x][pos.y] = 1.0
            } else {
                resistanceMap.getValue(pos.z)[pos.x][pos.y] = 0.0
            }
        }
    }

    fun update(screen: Screen, input: Input) {
        engine.update(GameContext(
                dungeon = this,
                screen = screen,
                input = input,
                player = this.player
        ))
    }

    private fun updateCamera() {
        val screenPosition = findPositionOf(player).get() - visibleOffset()
        val halfHeight = visibleSize.yLength / 2
        val halfWidth = visibleSize.xLength / 2
        if (screenPosition.y > halfHeight) {
            scrollForwardBy(screenPosition.y - halfHeight)
        }
        if (screenPosition.y < halfHeight) {
            scrollBackwardBy(halfHeight - screenPosition.y)
        }
        if (screenPosition.x > halfWidth) {
            scrollRightBy(screenPosition.x - halfWidth)
        }
        if (screenPosition.x < halfWidth) {
            scrollLeftBy(halfWidth - screenPosition.x)
        }
    }

    /**
     * Finds the [Position3D] of the given [Entity].
     */
    fun findPositionOf(entity: GameEntity<EntityType>): Maybe<Position3D> {
        return Maybe.ofNullable(entityPositionLookup[entity.id])
    }

    /**
     * Finds the [Position3D] of the StairsDown the given ZLevel
     */
    fun findPositionOfStairsDown(z: Int): Maybe<Position3D> {
        var position: Position3D? = null
        fetchBlocksAtLevel(z).forEach {
            if (it.block.isStairsDown) {
                position = it.position
            }
        }
        return Maybe.ofNullable(position)
    }

    /**
     * Finds the [Position3D] of the StairsUp for the given ZLevel
     */
    fun findPositionOfStairsUp(z: Int): Maybe<Position3D> {
        var position: Position3D? = null
        fetchBlocksAtLevel(z).forEach {
            if (it.block.isStairsUp) {
                position = it.position
            }
        }
        return Maybe.ofNullable(position)
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
        entity.position = position
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
        if (actualSize().containsPosition(position) && position.x >= 0 && position.y >= 0) {
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
            }
        } else {
            logger.info("Dungeon does not contain position: %s".format(position))
            return false
        }
        return true
    }

    fun fetchEntitiesAt(pos: Position3D): List<GameEntity<EntityType>> {
        return fetchBlockAt(pos).fold(whenEmpty = { kotlin.collections.listOf() }, whenPresent = {
            it.entities.toList()
        })
    }

    fun findItemsAt(pos: Position3D): List<GameEntity<Item>> {
        return fetchEntitiesAt(pos).filterType()
    }

    fun writeToFile() {
        for (z in 0 until actualSize().zLength) {
            File("logs/%s_dungeon_floor_%s.txt".format(System.getProperty("session.id"), z)).printWriter().use { out ->
                out.println("=================== Floor: %s ===================".format(z))
                for (y in 0 until actualSize().yLength) {
                    val sb = StringBuilder()
                    for (x in 0 until actualSize().xLength) {
                        val block = blocks[Position3D.create(x, y, z)]
                        block?.inFov = true
                        val c = block?.getEntityTile()?.asCharacterTile()?.get()?.character
                        sb.append(c)
                    }
                    out.println(sb)
                }
            }
        }
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}
