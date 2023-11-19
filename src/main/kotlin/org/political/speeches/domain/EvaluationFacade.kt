package org.political.speeches.domain

import org.political.speeches.DuplicateUrlException
import io.ktor.server.application.*
import io.ktor.util.*
import org.political.speeches.NoQueryParamStartsWithUrl

class EvaluationFacade {
    fun handleEvaluationRequest(call: ApplicationCall) : EvaluationResponse {
        val urls = extractUrls(call)

        val url2ContentLinesMap = UrlContentReader().readContents(urls)

        val speechList = SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        return SpeechFilter(speechList).filter()
    }

    private fun extractUrls(call: ApplicationCall): Set<String> {
        // TODO: Ask we are filtering query params starts with url,
        //  if the requirement is sequential url1 url2 implementation should change
        val urlSet = mutableSetOf<String>()

        call.request.queryParameters.forEach { key, values ->
            if (key.startsWith("url")) {
                values.forEach { value ->
                    if (urlSet.contains(value)) {
                        throw DuplicateUrlException("Duplicate URL found: $value")
                    }
                    urlSet.add(value)
                }
            }
        }
        if (urlSet.isEmpty()) {
            throw NoQueryParamStartsWithUrl()
        }
        return urlSet;
    }

}