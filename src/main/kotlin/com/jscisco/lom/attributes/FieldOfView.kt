package com.jscisco.lom.attributes

class FieldOfView(val radius: Double) {
    var fov: Array<DoubleArray> = Array<DoubleArray>(size= 0, init = { doubleArrayOf(0.0) })
}