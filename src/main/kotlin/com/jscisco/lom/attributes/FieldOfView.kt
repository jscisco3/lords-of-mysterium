package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

class FieldOfView(val radius: Double) : Attribute {
    var fov: Array<DoubleArray> = Array<DoubleArray>(size= 0, init = { doubleArrayOf(0.0) })
}