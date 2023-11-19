package com.example.domain

import io.ktor.server.application.*
import io.ktor.util.*

class EvaluationFacade {
    fun handleEvaluationRequest(call: ApplicationCall) : EvaluationResponse {
        val urls = extractUrls(call)

        val url2ContentLinesMap = UrlContentReader().readContents(urls)

        val speechList = SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        return SpeechFilter(speechList).filter()
    }

    private fun extractUrls(call: ApplicationCall): List<String> {
        // TODO: Ask we are filtering query params starts with url,
        //  if the requirement is sequential url1 url2 implementation should change
        return mutableListOf<String>().apply {
            call.request.queryParameters
                .filter { key, _ -> key.startsWith("url") }
                .forEach { _, list -> addAll(list) }
        }
    }

}