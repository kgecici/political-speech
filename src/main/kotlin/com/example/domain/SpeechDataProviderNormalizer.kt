package com.example.domain


import com.example.DataInconsistencyException
import com.example.UnexpectedFileFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Speech(val speaker: String, val topic: String, val date: LocalDate, var words: Int, val url: String = "")

class SpeechDataProvider {

    fun parseAndValidate(url2ContentLinesMap : Map<String,List<String>> ): List<Speech> {
        val speeches = mutableListOf<Speech>()

        val groupedSpeeches = mutableMapOf<Triple<String, String, LocalDate>, Speech>()

        for (csvUrl2Lines in url2ContentLinesMap) {
            for (line in csvUrl2Lines.value) { // skipping 1 line, it should be header
                val parts = line.split(";")

                if (parts.size != 4) {
                    throw UnexpectedFileFormat("Error: Unexpected file format. url: ${csvUrl2Lines.key} line: $line")
                }

                val speaker = parts[0].trim()
                val topic = parts[1].trim()
                val date = LocalDate.parse(parts[2].trim(), DateTimeFormatter.ISO_LOCAL_DATE)
                val words = parts[3].trim().toIntOrNull() ?: 0
                val key = Triple(speaker, topic, date)
                val speech = Speech(speaker, topic, date, words, csvUrl2Lines.key)

                val existingSpeech = groupedSpeeches[key]
                if (existingSpeech == null) {
                    groupedSpeeches.put(key, speech)
                    speeches.add(speech)
                    continue
                }

                if (existingSpeech.words != words) {
                    throw DataInconsistencyException(
                        "Different word count for speaker:${speaker} topic:${topic} date: ${date}. " +
                                "First url: ${existingSpeech.url} second url: ${csvUrl2Lines.key}"
                    )
                }
                // If we have same speaker + topic + date before, just skip second one
            }
        }
        return speeches
    }

}