package com.jscisco.lom.commands

/**
 * Represents the possible [Response]s when invoking a [Command]
 */
sealed class Response

/**
 * Use [Consumed] to indicate that the [Command] was handled
 */
object Consumed : Response()

/**
 * Use [Pass] to indicate that the [Command] was not handled
 */
object Pass : Response()

/**
 * Use [CommandResponse] to transform given [Command] or return a new one for downstream systems
 */
data class CommandResponse(val command: Command) : Response()