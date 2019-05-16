package com.jscisco.lom.dungeon

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.actor.Monster
import com.jscisco.lom.actor.Player
import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import com.jscisco.lom.builders.ItemFactory
import com.jscisco.lom.events.DoorOpenedEvent
import com.jscisco.lom.events.UpdateCamera
import com.jscisco.lom.events.UpdateFOW
import com.jscisco.lom.extensions.calculateFOV
import com.jscisco.lom.item.Item
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.ifPresent
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
              private val actualSize: Size3D,
              val player: Player) : GameArea<Tile, GameBlock> by GameAreaBuilder<Tile, GameBlock>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .withDefaultBlock(DEFAULT_BLOCK)
        .withLayersPerBlock(2)
        .build() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val fogOfWar: FogOfWar by lazy { FogOfWar(this) }
    val actors: MutableList<Actor> = mutableListOf()

    init {
        blocks.forEach { pos, block ->
            setBlockAt(pos, block)
            block.actor.ifPresent {
                it.position = pos
                actors.add(it)
            }
        }

        registerEvents()

        var playerStartPosition = findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), actualSize)
        while (playerStartPosition.isEmpty()) {
            playerStartPosition = findEmptyLocationWithin(Position3D.defaultPosition().withZ(0), actualSize)
        }

        addActor(player, playerStartPosition.get())
        player.position = playerStartPosition.get()

        fetchBlockAt(player.position).ifPresent {
            val sword = ItemFactory.sword()
            sword.position = player.position
            it.addItem(sword)
        }

        this.calculateFOV(player)
        fogOfWar.updateFOW()
        updateCamera()
    }


    private fun registerEvents() {

        Zircon.eventBus.subscribe<DoorOpenedEvent> {
            //            fogOfWar.updateFOW()
        }

        Zircon.eventBus.subscribe<UpdateFOW> {
            fogOfWar.updateFOW()
        }

        Zircon.eventBus.subscribe<UpdateCamera> {
            this.updateCamera()
        }
    }

    private fun updateCamera() {
        logger.trace("updating camera based on player position: %s".format(player.position.toString()))
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
                if (it.isOccupied.not() && it.isWalkable) {
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
     * return [true] if added, [false] othersise.
     */
    fun addActor(actor: Actor, position: Position3D): Boolean {
        var added = false
        fetchBlockAt(position).map {
            if (!it.isOccupied) {
                it.addActor(actor)
                actor.position = position
                added = true
                actors.add(actor)
            }
        }
        return added
    }

    fun removeActor(actor: Actor) {
        blocks.forEach {
            if (it.value.actor.isPresent && it.value.actor.get() == actor) {
                it.value.removeActor()
                actors.remove(actor)
                return@forEach
            }
        }
    }

    /**
     * Move an [Entity] to the desired [Postion3D]
     * @return true if the [Entity] was moved
     */
    fun moveEntity(actor: Actor, position: Position3D): Boolean {
        if (actualSize().containsPosition(position) && position.x >= 0 && position.y >= 0) {
            val oldPos = actor.position
            fetchBlockAt(oldPos).map {
                it.removeActor()
            }
            fetchBlockAt(position).map {
                it.addActor(actor)
            }
            actor.position = position
            return true
        } else {
            return false
        }
    }

    val monsters: List<Actor>
        get() = actors.filter {
            it is Monster
        }

    fun findItemsAt(pos: Position3D): List<Item> {
        var items = listOf<Item>()
        fetchBlockAt(pos).ifPresent {
            items = it.items
        }
        return items
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

}
