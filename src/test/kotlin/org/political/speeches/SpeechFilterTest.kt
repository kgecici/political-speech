package org.political.speeches

import org.political.speeches.domain.Evaluation
import org.political.speeches.domain.Speech
import org.political.speeches.domain.SpeechFilter
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SpeechFilterTest {


    @Test
    fun givenEmptyList_whenEvaluated_thenReturnsNullForEachResponse() {
        // GIVEN
        val evaluation = Evaluation(mostSpeechesInYear = 2020, mostSpeechesInTopic = "technology")

        // WHEN
        val speechFilter = SpeechFilter(emptyList())
        val evaluationResponse = speechFilter.filter()

        // THEN
        assertNull( evaluationResponse.mostSpeeches)
        assertNull( evaluationResponse.mostSecurity)
        assertNull( evaluationResponse.leastWordy)
    }

    @Test
    fun givenDifferentYearsForSpeakers_whenFindMostSpeeches_thenFindsOnly2013() {
        val testData = listOf(
            Speech("John Doe", "homeland security", LocalDate.of(2012, 1, 4), 1000),
            Speech("John Doe", "homeland security", LocalDate.of(2013, 1, 5), 1000),
            Speech("John Doe", "homeland security", LocalDate.of(2014, 1, 6), 1000),
            Speech("Jane Smith", "homeland security", LocalDate.of(2012, 1, 7), 2000),
            Speech("Jane Smith", "different topic", LocalDate.of(2013, 1, 8), 500),
            Speech("Jane Smith", "different topic", LocalDate.of(2014, 1, 8), 2000),
        )

        // WHEN
        val speechFilter = SpeechFilter(testData)
        val mostSpeechIn2013 = speechFilter.findMostSpeechSpeakers()

        // THEN
        assertEquals("John Doe", mostSpeechIn2013,
            "John Doe has 3000 words, Jane Smith has 500 words total in 2013")

    }

    @Test
    fun givenDifferentTopicsForSpeakers_whenFindMostSpeeches_thenFindsOnly2013() {
        val testData = listOf(
            Speech("John Doe", "homeland security", LocalDate.of(2012, 1, 4), 1000),
            Speech("John Doe", "homeland security", LocalDate.of(2013, 1, 5), 1000),
            Speech("John Doe", "homeland security", LocalDate.of(2014, 1, 6), 1000),
            Speech("Jane Smith", "homeland security", LocalDate.of(2012, 1, 7), 2000),
            Speech("Jane Smith", "different topic", LocalDate.of(2013, 1, 8), 500),
            Speech("Jane Smith", "different topic", LocalDate.of(2014, 1, 8), 2000),
        )

        // WHEN
        val speechFilter = SpeechFilter(testData)
        val homelandSecuritySpeeches = speechFilter.findHomelandSecuritySpeakers()

        // THEN
        assertEquals("John Doe", homelandSecuritySpeeches,
            "John Doe has 3000 words, Jane Smith has 2000 words total about homeland security")

    }

    @Test
    fun givenDifferentLessWordsInMultipleSpeech_whenFindLeastWordySpeakers_thenFindsWithSummingAllSpeeches() {
        val testData = listOf(
            Speech("John Doe", "homeland security", LocalDate.of(2012, 1, 4), 500),
            Speech("John Doe", "homeland security", LocalDate.of(2013, 1, 5), 500),
            Speech("John Doe", "homeland security", LocalDate.of(2014, 1, 6), 500),
            Speech("Jane Smith", "different topic", LocalDate.of(2014, 1, 8), 1450),
        )

        // WHEN
        val speechFilter = SpeechFilter(testData)
        val homelandSecuritySpeeches = speechFilter.findHomelandSecuritySpeakers()

        // THEN
        assertEquals("John Doe", homelandSecuritySpeeches,
            "John Doe has 1500 words, Jane Smith has 1450 words in total")

    }

    @Test
    fun givenSecuritySpeechWordsEqual_whenEvaluated_thenReturnsCommaSeperatedPoliticians() {
        val testData = listOf(
            Speech("John Doe", "homeland security", LocalDate.of(2012, 1, 4), 1000),
            Speech("Jane Smith", "homeland security", LocalDate.of(2013, 1, 5), 1000),
            Speech("John Doe", "homeland security", LocalDate.of(2013, 1, 6), 2000),
            Speech("Jane Smith", "homeland security", LocalDate.of(2013, 1, 7), 2000),
            Speech("Jane Smith", "different topic", LocalDate.of(2013, 1, 8), 10000),
        )

        // WHEN
        val speechFilter = SpeechFilter(testData)
        val evaluationResponse = speechFilter.filter()

        // THEN
        assertEquals(
            "Jane Smith",
            evaluationResponse.mostSpeeches,
            "John Doe has 3000 words, Jane Smith has 13000 words total in 2013"
        )
        assertEquals(
            "John Doe, Jane Smith",
            evaluationResponse.mostSecurity,
            "John Doe and Jane Smith have 3000 words each in homeland security"
        )
        assertEquals(
            "John Doe", evaluationResponse.leastWordy, "John Doe has 3000 words in general"
        )
    }

    @Test
    fun givenDistinctSpeakers_whenEvaluated_thenReturnsCorrectResponse() {
        // GIVEN
        val testData = listOf(
            Speech("John Doe", "homeland security", LocalDate.of(2012, 1, 4), 15000),
            Speech("Jane Smith", "homeland security", LocalDate.of(2013, 1, 4), 3000),
            Speech("Alice Johnson", "education", LocalDate.of(2013, 1, 4), 1800),
            Speech("Ashil Topaco", "education", LocalDate.of(2013, 1, 4), 1800),
            Speech("Mike Thompson", "education", LocalDate.of(2013, 1, 4), 1800),
            Speech("Bob Brown", "technology", LocalDate.of(2013, 1, 4), 2200)
        )

        // WHEN
        val speechFilter = SpeechFilter(testData)
        val evaluationResponse = speechFilter.filter()

        // THEN
        assertEquals(
            "Jane Smith",
            evaluationResponse.mostSpeeches,
            "Since speakers are distinct, in 2013 Jane Smith has most speeches"
        )
        assertEquals(
            "John Doe", evaluationResponse.mostSecurity, "John Doe has most words in homeland security including 2012"
        )
        assertEquals(
            "Alice Johnson, Ashil Topaco, Mike Thompson",
            evaluationResponse.leastWordy,
            "Alice Johnson, Ashil Topaco, Mike Thompson have 1800 words each in education"
        )
    }


}