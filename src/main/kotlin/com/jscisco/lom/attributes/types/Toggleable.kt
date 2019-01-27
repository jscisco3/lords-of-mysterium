package com.jscisco.lom.attributes.types

import com.jscisco.lom.attributes.Openable
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.attribute
import org.hexworks.amethyst.api.entity.EntityType

interface Toggleable : EntityType

val GameEntity<Toggleable>.openable: Openable
    get() = attribute()

