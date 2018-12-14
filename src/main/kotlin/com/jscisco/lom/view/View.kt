package com.jscisco.lom.view

import org.hexworks.zircon.api.screen.Screen

interface View {

    val screen: Screen

    fun dock()

}