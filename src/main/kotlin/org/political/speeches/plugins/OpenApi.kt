package org.political.speeches.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Application.configureSwagger() {

    routing {
        swaggerUI(path = "openapi")
    }

    environment.monitor.subscribe(ApplicationStarted) {
        val logger = LoggerFactory.getLogger(Application::class.java)
        val host = environment.config.propertyOrNull("ktor.deployment.host")?.getString() ?: "localhost"
        val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString()?.toInt() ?: 8080
        logger.info("see swagger at http://$host:$port/openapi")
    }
}
