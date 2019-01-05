package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.ItemHolder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.zircon.api.data.impl.Position3D

data class PickItemUp(override val context: GameContext,
                      override val source: GameEntity<ItemHolder>,
                      val position: Position3D) : GameCommand<ItemHolder>