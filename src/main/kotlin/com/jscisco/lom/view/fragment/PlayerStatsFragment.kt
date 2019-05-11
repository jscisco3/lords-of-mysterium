package com.jscisco.lom.view.fragment

import com.jscisco.lom.actor.Player
import com.jscisco.lom.configuration.GameConfiguration.SIDEBAR_WIDTH
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment

class PlayerStatsFragment(player: Player) : Fragment {

    override val root: Component by lazy {
        var componentHeight = 0
        val innerWidth = SIDEBAR_WIDTH - 2

//        val nameComponent = player.nameAttribute.toComponent(innerWidth)
//        componentHeight += nameComponent.height

        val healthComponent = player.health.toComponent(innerWidth)
        healthComponent.moveDownBy(componentHeight)
        componentHeight += healthComponent.height

        Components.panel()
                .withSize(SIDEBAR_WIDTH, componentHeight + 2)
                .wrapWithBox()
                .build().apply {
                    //                    addComponent(nameComponent)
                    addComponent(healthComponent)
//                    addComponent(statBlockComponent)
                }
    }
}
//
//        val statBlockComponent = player.statBlock.toComponent(innerWidth)
//        statBlockComponent.moveDownBy(componentHeight)
//        componentHeight += statBlockComponent.height
//

//    }
//
//}