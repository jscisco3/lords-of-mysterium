package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.ItemHolder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

data class DropItem(override val context: GameContext,
                    override val source: GameEntity<ItemHolder>,
                    val item: GameEntity<Item>,
                    val position: Position3D) : GameCommand<EntityType>