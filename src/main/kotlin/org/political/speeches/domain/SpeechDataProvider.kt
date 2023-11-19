package org.political.speeches.domain


import org.political.speeches.DataInconsistencyException
import org.political.speeches.FieldParseException
import org.political.speeches.UnexpectedFileFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class Speech(val speaker: String, val topic: String, val date: LocalDate, var words: Int, val url: String = "")

data class SpeechRaw(val speaker: String, val topic: String, val date: String, val words: String)
class SpeechDataProvider {

    fun parseAndValidate(url2ContentLinesMap: Map<String, List<String>>): List<Speech> {
        val speeches = mutableListOf<Speech>()

        val groupedSpeeches = mutableMapOf<Triple<String, String, LocalDate>, Speech>()

        for (csvUrl2Lines in url2ContentLinesMap) {
            for (line in csvUrl2Lines.value) {
                val speechRaw = parseSpeechRaw(line, csvUrl2Lines.key)
                val date = parseDate(speechRaw.date, csvUrl2Lines.key, line)
                val words = parseInt(speechRaw.words, csvUrl2Lines.key, line)

                val speech = Speech(speechRaw.speaker, speechRaw.topic, date, words, url = csvUrl2Lines.key)

                val key = Triple(speechRaw.speaker, speechRaw.topic, date)
                handleExistingSpeech(groupedSpeeches, speeches, key, speech)

            }
        }
        return speeches
    }

    private fun handleExistingSpeech(
        groupedSpeeches: MutableMap<Triple<String, String, LocalDate>, Speech>,
        speeches: MutableList<Speech>,
        key: Triple<String, String, LocalDate>,
        speech: Speech
    ) {
        val existingSpeech = groupedSpeeches[key]
        if (existingSpeech == null) {
            groupedSpeeches.put(key, speech)
            speeches.add(speech)
        } else if (existingSpeech.words != speech.words) {
            throw DataInconsistencyException(
                "Different word count for speaker:${speech.speaker} topic:${speech.topic} date: ${speech.date}. " +
                        "First url: ${existingSpeech.url} second url: ${speech.url}"
            )
        }
    }

    private fun parseSpeechRaw(line: String, url: String): SpeechRaw {
        val parts = line.split(";")
        if (parts.size != 4) {
            throw UnexpectedFileFormat("Csv file should have 4 columns. url: $url line: $line")
        }
        return SpeechRaw(
            speaker = parts[0].trim(),
            topic = parts[1].trim(),
            date = parts[2].trim(),
            words = parts[3].trim()
        )
    }

    private fun parseDate(dateString: String, url: String, line: String): LocalDate {
        return try {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: DateTimeParseException) {
            throw FieldParseException("Speech date cannot be parsed. Invalid date format. url: $url line: $line")
        }
    }

    private fun parseInt(valueString: String, url: String, line: String): Int {
        return try {
            valueString.toInt()
        } catch (e: NumberFormatException) {
            throw FieldParseException("Word count cannot be parsed as an integer. url: $url line: $line")
        }
    }
}