package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

/**
 * Track the base armor stats:
 *  ac: physical defense
 *  ev: chance to dodge
 */
data class Armor(val ac: Int, val ev: Int) : Attribute