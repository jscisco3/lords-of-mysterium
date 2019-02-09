package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.builders.GameBlockFactory
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.shape.FilledRectangleFactory
import java.util.*
import kotlin.math.ceil
import kotlin.random.Random

//TODO: Fix this at some point
class BSPTreeGenerationStrategy(private val dungeonSize: Size3D) : GenerationStrategy(dungeonSize) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val rootRegion = Region(dungeonSize.to2DSize(), Position.defaultPosition())

    override fun generateDungeon(): MutableMap<Position3D, GameBlock> {
        initializeWalls()
        for (z in 0 until dungeonSize.zLength) {
            // Create the BSP tree for each floor
            val bspTree = createBSPTree(
                    BSPNode(rootRegion),
                    iterations = 5
            )
            // For each leaf node, carve out a small room
            bspTree.leafNodes().forEach {
                val fillWidth = Random.nextInt(4, it.value.size.width)
                val fillHeight = Random.nextInt(4, it.value.size.height)
                val topLeftX = (1..(it.value.size.width - fillWidth)).random()
                val topLeftY = (1..(it.value.size.height - fillHeight)).random()
                logger.info("(%s,%s) | W:%s | H:%s |  %sx%s".format(topLeftX, topLeftY, it.value.size.width, it.value.size.height, fillWidth, fillHeight))
                FilledRectangleFactory.buildFilledRectangle(
                        it.value.topLeftCorner.withRelative(Position.create(topLeftX, topLeftY)),
                        Size.create(
                                fillWidth,
                                fillHeight
                        )
                ).forEach { pos ->
                    blocks[Position3D.from2DPosition(pos, z)] = GameBlockFactory.floor()
                }
            }
        }
        initializeOutsideWalls()
        writeDungeonToFile()
        return blocks
    }

    fun createBSPTree(root: BSPNode<Region>, iterations: Int): BSPNode<Region> {
        for (i in 0 until iterations) {
            root.leafNodes().forEach {
                splitRegion(it)
            }
        }
        return root
    }

    fun splitRegion(node: BSPNode<Region>) {
        val region = node.value
        val splitRegions = region.splitRegion(horizontal = Random.nextBoolean(), splitMultiplier = Random.nextDouble(0.2, 0.8))
        if (splitRegions.isNotEmpty()) {
            node.link(
                    BSPNode(splitRegions[0]),
                    BSPNode(splitRegions[1])
            )
        }

    }


    data class BSPNode<T>(val value: T,
                          var leftNode: BSPNode<T>? = null,
                          var rightNode: BSPNode<T>? = null,
                          var depth: Int = 0) {

        fun link(left: BSPNode<T>?, right: BSPNode<T>?) = this.apply {
            linkLeft(left).linkRight(right)
        }

        fun linkLeft(left: BSPNode<T>?) = this.apply {
            leftNode = left
        }

        fun linkRight(right: BSPNode<T>?) = this.apply {
            rightNode = right
        }

        fun depth(value: Int) = this.apply {
            depth = value
        }

        fun leafNodes(): List<BSPNode<T>> {
            val leaves = mutableListOf<BSPNode<T>>()
            if (this.isLeaf) {
                leaves.add(this)
            } else {
                if (this.leftNode != null) {
                    leaves.addAll(this.leftNode!!.leafNodes())
                }
                if (this.rightNode != null) {
                    leaves.addAll(this.rightNode!!.leafNodes())
                }
            }
            return leaves
        }

        val isLeaf: Boolean
            get() = leftNode == null && rightNode == null

        fun bfsTraversal(): Iterable<BSPNode<T>> {
            val queue = LinkedList<BSPNode<T>>()
            queue.add(this)

            val traversalList = mutableListOf<BSPNode<T>>()

            while (queue.isNotEmpty()) {
                val currentNode = queue.poll()
                val depth = currentNode.depth
                // First add the left node
                if (currentNode.leftNode != null) {
                    queue.add(currentNode.leftNode!!.depth(depth + 1))
                }
                // Then add the right node
                if (currentNode.rightNode != null) {
                    queue.add(currentNode.rightNode!!.depth(depth + 1))
                }
                traversalList.add(currentNode)
            }
            return traversalList.asIterable()
        }

    }

    class Region(val size: Size, val topLeftCorner: Position) {

        // Split a region in two, and return the resultant regions
        fun splitRegion(horizontal: Boolean, splitMultiplier: Double = 0.5, minWidth: Int = 5, minHeight: Int = 5): List<Region> {
            return if (horizontal) {
                val splitPosition = Position.create(topLeftCorner.x, ceil(topLeftCorner.y + size.height * splitMultiplier).toInt())
                var firstRegionHeight = ceil(size.height * splitMultiplier).toInt()
                if (firstRegionHeight < minHeight) {
                    firstRegionHeight = minHeight
                }
                if (size.height - firstRegionHeight < minHeight) {
                    firstRegionHeight -= (minHeight - (size.height - firstRegionHeight))
                }
                if (firstRegionHeight < minHeight) {
                    return listOf()
                }
                listOf(
                        Region(Size.create(size.width, firstRegionHeight), topLeftCorner),
                        Region(Size.create(size.width, size.height - firstRegionHeight), splitPosition)
                )
            } else {
                val splitPosition = Position.create(topLeftCorner.x + ceil(size.width * splitMultiplier).toInt(), topLeftCorner.y)
                var firstRegionWidth = ceil(size.width * splitMultiplier).toInt()
                if (firstRegionWidth < minWidth) {
                    firstRegionWidth = minWidth
                }
                if (size.width - firstRegionWidth < minWidth) {
                    firstRegionWidth -= (minHeight - (size.width - firstRegionWidth))
                }
                if (firstRegionWidth < minWidth) {
                    return listOf()
                }
                listOf(
                        Region(Size.create(firstRegionWidth, size.height), topLeftCorner),
                        Region(Size.create(size.width - firstRegionWidth, size.height), splitPosition)
                )
            }
        }

        @Override
        override fun toString(): String {
            return "%s ; %s ".format(topLeftCorner, size)
        }
    }
}