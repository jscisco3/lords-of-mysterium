package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.attributes.types.health
import com.jscisco.lom.configuration.GameConfiguration.SIDEBAR_WIDTH
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.nameAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class PlayerStatsFragment(player: GameEntity<Player>) : Fragment {

    override val root: Component by lazy {
        var componentHeight = 0

        val nameComponent = player.nameAttribute.toComponent(SIDEBAR_WIDTH)
        componentHeight += nameComponent.height

        val healthComponent = player.health.toComponent(SIDEBAR_WIDTH)
        healthComponent.moveDownBy(componentHeight)
        componentHeight += healthComponent.height

        Components.panel()
                .withSize(SIDEBAR_WIDTH, componentHeight + 2)
                .build().apply {
                    addComponent(nameComponent)
                    addComponent(healthComponent)
                }
    }

}