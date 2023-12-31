package org.political.speeches

import org.political.speeches.domain.SpeechDataProvider
import kotlin.test.*

class SpeechDataProviderTest {

    @Test
    fun givenTwoUrlHasSameData_whenParsedAndNormalized_thenRemovesDuplications() {
        // GIVEN
        val csvUrl1 = "http://example.com/data1.csv"
        val csvUrl2 = "http://example.com/data2.csv"

        val lines1 = listOf(
            "John Doe;Technology;2022-01-01;500",
            "Jane Smith;Education;2022-02-01;700"
        )

        val lines2 = listOf(
            "John Doe;Technology;2022-01-01;500",
            "Jane Smith;Education;2022-02-01;700"
        )

        val url2ContentLinesMap = mapOf(
            csvUrl1 to lines1,
            csvUrl2 to lines2
        )

        // WHEN
        val parseAndValidate = SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        // THEN
        assertEquals(2, parseAndValidate.size, "There are 4 lines but contant 2 distinct, so it should return 2 speech")
    }

    @Test(expected = DataInconsistencyException::class)
    fun givenTwoUrlHasSameKeyButDifferentWordCount_whenParsedAndNormalized_thenThrowsError() {
        // GIVEN
        val csvUrl1 = "http://example.com/data1.csv"
        val csvUrl2 = "http://example.com/data2.csv"

        val lines1 = listOf(
            "John Doe;Technology;2022-01-01;500",
            "Jane Smith;Education;2022-02-01;700"
        )

        val lines2 = listOf(
            "John Doe;Technology;2022-01-01;500",
            "Jane Smith;Education;2022-02-01;701"
        )

        val url2ContentLinesMap = mapOf(
            csvUrl1 to lines1,
            csvUrl2 to lines2
        )

        // WHEN
        SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        // THEN Throws exception
    }


    @Test(expected = UnexpectedFileFormat::class)
    fun givenFileDoesNotHaveValidCsv_whenParsedAndNormalized_thenThrowsError() {
        // GIVEN
        val url2ContentLinesMap = mapOf(
            "http://example.com/data1.csv" to listOf("John Doe;Technology;2022-01-01"),
        )

        // WHEN
        SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        // THEN Throws exception
    }


    @Test(expected =  FieldParseException::class)
    fun givenFileHasWrongDateFormat_whenParsedAndNormalized_thenThrowsError() {
        // GIVEN
        val url2ContentLinesMap = mapOf(
            "http://example.com/data1.csv" to listOf("John Doe;Technology;2022-99-99;200"),
        )

        // WHEN
        SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        // THEN Throws exception
    }

    @Test(expected =  FieldParseException::class)
    fun givenFileHasWrongWordCount_whenParsedAndNormalized_thenThrowsError() {
        // GIVEN
        val url2ContentLinesMap = mapOf(
            "http://example.com/data1.csv" to listOf("John Doe;Technology;2022-01-01;notnumber"),
        )

        // WHEN
        SpeechDataProvider().parseAndValidate(url2ContentLinesMap)

        // THEN Throws exception
    }
}