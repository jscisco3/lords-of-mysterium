package com.jscisco.lom.triggers

import com.jscisco.lom.trigger.Trigger
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.events.api.Event
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.internal.Zircon
import org.junit.jupiter.api.Test

class TestTrigger {

    @Test
    fun `a trigger should have an active subscription`() {
        val trigger = Trigger {
            Zircon.eventBus.subscribe<TestEvent> {}
        }
        Assertions.assertThat(trigger.active).isTrue()
    }

    @Test
    fun `a trigger that has been deactivated should have a cancelled subscription`() {
        val trigger = Trigger {
            Zircon.eventBus.subscribe<TestEvent> {}
        }
        trigger.deactivate()
        Assertions.assertThat(trigger.inactive).isTrue()
    }

    @Test
    fun `a trigger that is active should handle an event`() {
        val testModel = TestModel("NEW MODEL")
        val trigger = Trigger {
            Zircon.eventBus.subscribe<TestEvent> { (model) ->
                model.text = "success"

            }
        }
        Assertions.assertThat(trigger.active).isTrue()
        Zircon.eventBus.publish(TestEvent(testModel))
        Assertions.assertThat(testModel.text).isEqualTo("success")
    }

    @Test
    fun `a trigger that is inactive should not handle an event`() {
        val testModel = TestModel("NEW MODEL")
        val trigger = Trigger {
            Zircon.eventBus.subscribe<TestEvent> { (model) ->
                model.text = "success"

            }
        }.apply { this.deactivate() }
        Zircon.eventBus.publish(TestEvent(testModel))
        Assertions.assertThat(testModel.text).isEqualTo("NEW MODEL")
    }

    data class TestEvent(val testModel: TestModel) : Event

    data class TestModel(var text: String)

}