package com.jscisco.lom.view.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.extensions.handleMouseEvents
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

class CloseButtonFragment(modal: Modal<EmptyModalResult>, parent: Container) : Fragment {
    override val root = Components.button()
            .withText("X")
            .withAlignmentWithin(parent, ComponentAlignment.TOP_RIGHT)
            .build().apply {
                handleMouseEvents(MouseEventType.MOUSE_RELEASED) { _, _ ->
                    modal.close(EmptyModalResult)
                    Processed
                }
            }
}