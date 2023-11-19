package org.political.speeches.plugins

import org.political.speeches.ValidationExceptionBase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.political.speeches.domain.EvaluationFacade

fun Application.configureRouting() {

    routing {
        get("/evaluation") {
            val evaluationResponse = EvaluationFacade().handleEvaluationRequest(call)
            call.respond(evaluationResponse)
        }
    }
}
