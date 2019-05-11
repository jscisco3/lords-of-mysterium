package com.jscisco.lom.view.fragment

import com.jscisco.lom.item.Item
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.RadioButtonGroup
import org.hexworks.zircon.api.extensions.onSelection

class ItemListFragment(items: List<Item>, width: Int) : Fragment {

    val logger: Logger = LoggerFactory.getLogger(javaClass)
    var selectedItem: Maybe<Item> = Maybe.ofNullable(null)

    private val rgb: RadioButtonGroup

    fun fetchSelectedItem(): Maybe<Item> {
        return selectedItem
    }

    fun removeSelectedItem() {
        rgb.fetchSelectedOption().map {
            rgb.removeOption(it)
        }
    }

    override val root = Components.panel()
            .withSize(width, items.size + 2)
            .build().apply {
                rgb = Components.radioButtonGroup()
                        .withSize(width, items.size)
                        .withPosition(0, 2)
                        .build()
                items.forEach { item ->
                    rgb.addOption(item.id.toString(), item.name)
                }
                rgb.onSelection {
                    selectedItem = Maybe.of(items.filter { item ->
                        item.id.toString() == it.key
                    }.last())
                }
                this.addComponent(Components.header()
                        .withText("Items"))
                addComponent(rgb)
            }
}