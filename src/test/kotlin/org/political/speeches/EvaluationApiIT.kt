package org.political.speeches

import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class EvaluationApiIT {
    @Test
    fun givenParametersDoesNotHaveUrlPrefix_whenApiCalled_ThenBadRequest() = testApplication {
        val response = client.get("/evaluation")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun givenTwoUrlHasSameAddress_whenApiCalled_ThenBadRequest() = testApplication {
        val response = client.get("/evaluation?urlA=x&urlB=x")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun givenNonExistingUrl_whenApiCalled_ThenBadRequest() = testApplication {
        val response = client.get("/evaluation?urlA=http://notexistingurl.no.value/xyz.csv")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}