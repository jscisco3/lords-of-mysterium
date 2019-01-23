package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

class FieldOfView(val radius: Double = 10.0) : Attribute {
    var fov = Array<DoubleArray>(size = 0, init = { doubleArrayOf(0.0) })
}