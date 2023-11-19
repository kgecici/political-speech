package org.political.speeches

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.political.speeches.plugins.configureExceptionHandling
import org.political.speeches.plugins.configureRouting
import org.political.speeches.plugins.configureSerialization

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    configureSerialization()
    configureExceptionHandling()
    configureRouting()
}
