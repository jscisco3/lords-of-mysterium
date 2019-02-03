package com.jscisco.lom.trigger

import org.hexworks.cobalt.events.api.Subscription

class Trigger(subscriptionFunction: () -> Subscription) {

    private val fn: () -> Subscription = subscriptionFunction
    private var subscription: Subscription = fn()

    fun deactivate() {
        subscription.cancel()
    }

    fun activate() {
        subscription = fn()
    }

}