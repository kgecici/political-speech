package com.example

import com.example.domain.UrlContentReader
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UrlContentReaderIntegrationTest {

    private val serverPort = 8088 // TODO make it random port
    private lateinit var server: NettyApplicationEngine

    @Before
    fun setUp() {
        server = embeddedServer(Netty, port = serverPort, module = Application::testModule)
        server.start()
    }

    @After
    fun tearDown() {
        server.stop(0, 0)
    }

    @Test(expected = FileFormatSecurityException::class)
    fun givenMaliciousUrl_thenReadOverHttp_thenFails() {
        // WHEN
        UrlContentReader().readContents(listOf("http://localhost:$serverPort/malicious.csv"))
    }

    @Test
    fun givenOneValidUrl_thenReadOverHttp_thenReturnsSuccessfully() {
        // GIVEN

        // WHEN
        val response = UrlContentReader().readContents(listOf("http://localhost:$serverPort/data1.csv"))

        // THEN
        assertNotNull(response)
        assertEquals(1, response.size)
        assertEquals(3, response.values.first().size)
    }

    @Test
    fun givenTwoValidUrl_thenReadOverHttp_thenReturnsSuccessfully() {
        // GIVEN

        // WHEN
        val response = UrlContentReader().readContents(listOf("http://localhost:$serverPort/data1.csv",
            "http://localhost:$serverPort/data1-duplicate.csv"))

        // THEN
        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(3, response.values.first().size)
        assertEquals(3, response.values.drop(1).first().size)
    }
}