package com.jscisco.lom.dungeon.strategies

import com.jscisco.lom.dungeon.strategies.BSPTreeGenerationStrategy.BSPNode
import com.jscisco.lom.dungeon.strategies.BSPTreeGenerationStrategy.Region
import org.assertj.core.api.Assertions
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TestBSPDungeonGeneration {

    @Nested
    inner class TestRegionCreation {

        @Test
        fun `when splitting horizontal regions in half, they should look as I expect`() {
            val region = Region(Size.create(100, 100), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = true)
            Assertions.assertThat(splitRegions.size).isEqualTo(2)
            // The first sub-region should have the same topLeftCorner as the original region
            Assertions.assertThat(splitRegions[0].topLeftCorner).isEqualTo(region.topLeftCorner)
            // The two sub-regions should have the same height as the original region
            splitRegions.forEach {
                println(it)
            }
        }

        @Test
        fun `when splitting horizontally, the two sub regions should have the same height as the original region`() {
            val region = Region(Size.create(17, 35), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = true)
            Assertions.assertThat(splitRegions[0].size.height + splitRegions[1].size.height).isEqualTo(region.size.height)
        }

        @Test
        fun `when splitting vertical regions, they should look as I expect`() {
            val region = Region(Size.create(100, 100), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = false)
            Assertions.assertThat(splitRegions.size).isEqualTo(2)
            Assertions.assertThat(splitRegions[0].topLeftCorner).isEqualTo(region.topLeftCorner)
            Assertions.assertThat(splitRegions[1].topLeftCorner).isEqualTo(
                    region.topLeftCorner.withRelative(Position.create(region.topLeftCorner.x + splitRegions[1].size.width, region.topLeftCorner.y))
            )
            splitRegions.forEach {
                println(it)
            }
        }

        @Test
        fun `when splitting vertically, the two sub regions should have the same height as the original region`() {
            val region = Region(Size.create(53, 35), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = false)
            Assertions.assertThat(splitRegions[0].size.width + splitRegions[1].size.width).isEqualTo(region.size.width)
        }

        @Test
        fun `if I split a region and it's subregion they should look like what I expect`() {
            val region = Region(Size.create(65, 75), Position.defaultPosition())
            val splitRegions = region.splitRegion(true)
            val splitRegions2 = splitRegions[1].splitRegion(false)

            splitRegions.forEach {
                println(it)
            }
            splitRegions2.forEach {
                println(it)
            }
        }
    }

    @Nested
    inner class TestCreateBSPTree {

        @Test
        fun `a new BSP trees should have null children`() {
            val bspTree = BSPNode(
                    Region(Size.create(10, 10), Position.create(0, 0))
            )
            Assertions.assertThat(bspTree.leftNode).isNull()
            Assertions.assertThat(bspTree.rightNode).isNull()
        }

        @Test
        fun `a new BSP tree should a bfs traversal of just the root node`() {
            val bspTree = BSPNode(
                    Region(Size.create(10, 10), Position.create(0, 0))
            )
            Assertions.assertThat(bspTree.bfsTraversal().toList().size).isEqualTo(1)
        }

        @Test
        fun `We should be able to link a tree up`() {
            val region = Region(Size.create(30, 30), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = true)

            val bspTree = BSPNode(region)
            bspTree.link(BSPNode(splitRegions[0]), BSPNode(splitRegions[1]))

            Assertions.assertThat(bspTree.bfsTraversal().toList().size).isEqualTo(3)
        }

        @Test
        fun `A tree with no children should only have the root as the leave nodes`() {
            val bspTree = BSPNode(
                    Region(Size.create(10, 10), Position.defaultPosition())
            )
            val leaves = bspTree.leafNodes()
            Assertions.assertThat(leaves.last()).isEqualTo(bspTree)
        }

        @Test
        fun `A tree with some leaves should not have the root in the leaf nodes`() {
            val region = Region(Size.create(30, 30), Position.create(0, 0))
            val splitRegions = region.splitRegion(horizontal = true)

            val bspTree = BSPNode(region)
            bspTree.link(BSPNode(splitRegions[0]), BSPNode(splitRegions[1]))

            val leaves = bspTree.leafNodes()
            Assertions.assertThat(leaves.size).isEqualTo(2)
        }

        @Test
        fun `When we iterate some amount of times, we should end up with the expected number of nodes`() {
            val root = BSPNode(
                    Region(Size.create(80, 50), Position.defaultPosition())
            )
            val bspDungeonGeneration = BSPTreeGenerationStrategy(Size3D.create(80, 50, 1))
            val tree = bspDungeonGeneration.createBSPTree(root, 3)
            Assertions.assertThat(tree.leafNodes().size).isEqualTo(8)
            tree.leafNodes().forEach {
                println(it)
            }
        }
    }

    @Test
    fun `We should be able to generate a BSP dungeon`() {
        val strategy = BSPTreeGenerationStrategy(Size3D.create(80, 50, 1))
        strategy.generateDungeon()
    }
}