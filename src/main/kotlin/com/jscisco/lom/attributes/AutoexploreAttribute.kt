package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

class AutoexploreAttribute : Attribute {
    var costMap = Array<DoubleArray>(size = 0, init = { doubleArrayOf(0.0) })
}