package org.political.speeches.plugins

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.political.speeches.ValidationExceptionBase
import io.ktor.server.plugins.statuspages.*

fun Application.configureExceptionHandling() {

    install(StatusPages) {
        exception<ValidationExceptionBase> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, "Validation Error: ${cause.localizedMessage}")
        }
        exception<Exception> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${cause.localizedMessage}")
        }
    }


}
