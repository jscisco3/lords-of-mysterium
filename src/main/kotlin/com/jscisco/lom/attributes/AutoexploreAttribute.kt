package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import squidpony.squidai.DijkstraMap

class AutoexploreAttribute : Attribute {
    val dijkstraMap = DijkstraMap()
}