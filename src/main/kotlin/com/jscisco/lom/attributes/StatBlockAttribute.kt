package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

data class StatBlockAttribute(
        var strength: Int = 0,
        var intelligence: Int = 0,
        var constitution: Int = 0,
        var agility: Int = 0,
        var perception: Int = 0
) : Attribute