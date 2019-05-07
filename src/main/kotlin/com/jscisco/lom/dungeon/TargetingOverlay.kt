//package com.jscisco.lom.dungeon
//
//import com.jscisco.lom.attributes.LookingAttribute
//import com.jscisco.lom.attributes.types.Player
//import com.jscisco.lom.builders.GameTileBuilder
//import com.jscisco.lom.extensions.GameEntity
//import com.jscisco.lom.extensions.position
//import com.jscisco.lom.extensions.whenHasAttribute
//import org.hexworks.cobalt.datatypes.extensions.ifPresent
//import org.hexworks.cobalt.logging.api.Logger
//import org.hexworks.cobalt.logging.api.LoggerFactory
//import org.hexworks.zircon.api.Layers
//import org.hexworks.zircon.api.data.Position
//import org.hexworks.zircon.api.data.impl.Position3D
//import org.hexworks.zircon.api.data.impl.Size3D
//import org.hexworks.zircon.api.graphics.Layer
//import squidpony.squidmath.DDALine
//
//class TargetingOverlay(val dungeon: Dungeon, val player: GameEntity<Player>, val size: Size3D) {
//
//    private val logger: Logger = LoggerFactory.getLogger(javaClass)
//
//    private val lookingOverlay: Layer = Layers.newBuilder()
//            .withSize(size.to2DSize())
//            .build()
//            .fill(GameTileBuilder.EMPTY)
//
//    init {
//        dungeon.pushOverlayAt(lookingOverlay, player.position.z)
//    }
//
//    fun updateOverlay() {
//        clearOverlay()
//        player.whenHasAttribute<LookingAttribute> {
//            val coords = DDALine.line(player.position.x, player.position.y, it.position.x, it.position.y)
//            var blockingIdx = -1
//            coords.forEachIndexed { index, coord ->
//                dungeon.fetchBlockAt(Position3D.create(coord.x, coord.y, player.position.z)).ifPresent { block ->
//                    if (block.blocksVision && blockingIdx < 0) {
//                        blockingIdx = index
//                    }
//                }
//                logger.debug("blocking index: %s | index: %s | coordinate: %s".format(blockingIdx, index, coord.toString()))
//                // IF there is something blocking the Line and we are at or past it, show in red
//                if (index >= blockingIdx && blockingIdx != -1) {
//                    lookingOverlay.setTileAt(Position.create(coord.x, coord.y), GameTileBuilder.LOOKING_LINE_BLOCKED)
//                } else {
//                    lookingOverlay.setTileAt(Position.create(coord.x, coord.y), GameTileBuilder.LOOKING_LINE)
//                }
//            }
//            lookingOverlay.setTileAt(Position.create(coords.last().x, coords.last().y), GameTileBuilder.LOOKING_LINE_SELECTED)
//            // Have the line start off the player
//            lookingOverlay.setTileAt(player.position.to2DPosition(), GameTileBuilder.EMPTY)
//        }
//    }
//
//    fun clearOverlay() {
//        lookingOverlay.fetchPositions().forEach {
//            lookingOverlay.setTileAt(it, GameTileBuilder.EMPTY)
//        }
//    }
//}