package com.example

import com.example.domain.SpeechProcessor
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class SpeechProcessorTest {

    @Test
    fun `test readSpeechesFromFile function`() {
        val testData = """
            Alexander Abel; education policty; 2012-10-30; 5310
            Bernhard Belling; coal subsidies; 2012-11-05; 1210
            Caesare Collins; coal subsidies; 2012-11-06; 1119
            Alexander Abel; homeland security; 2012-12-11; 911
        """.trimIndent()

        // TODO: Create under temp folder
        val testFilePath = "test_file" + UUID.randomUUID() + ".txt"

        File(testFilePath).writeText(testData)

        val speechList = SpeechProcessor().readSpeechesFromFile(testFilePath)

        assertEquals(4, speechList.size)

        assertEquals("Alexander Abel", speechList[0].speaker)
        assertEquals("education policty", speechList[0].topic)
        assertEquals("2012-10-30", speechList[0].date)
        assertEquals(5310, speechList[0].words)

        assertEquals("Bernhard Belling", speechList[1].speaker)
        assertEquals("coal subsidies", speechList[1].topic)
        assertEquals("2012-11-05", speechList[1].date)
        assertEquals(1210, speechList[1].words)

        assertEquals("Caesare Collins", speechList[2].speaker)
        assertEquals("coal subsidies", speechList[2].topic)
        assertEquals("2012-11-06", speechList[2].date)
        assertEquals(1119, speechList[2].words)

        assertEquals("Alexander Abel", speechList[3].speaker)
        assertEquals("homeland security", speechList[3].topic)
        assertEquals("2012-12-11", speechList[3].date)
        assertEquals(911, speechList[3].words)

        // Sanal dosyayı temizle
        File(testFilePath).delete()
    }
}