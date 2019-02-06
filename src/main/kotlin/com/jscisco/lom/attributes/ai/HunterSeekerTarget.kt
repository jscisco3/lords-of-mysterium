package com.jscisco.lom.attributes.ai

import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType

data class HunterSeekerTarget(var entity: GameEntity<EntityType>?) : Attribute