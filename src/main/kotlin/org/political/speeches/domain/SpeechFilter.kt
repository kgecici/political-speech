package org.political.speeches.domain

import org.political.speeches.extension.joinKeysWhenValuesEqualTo


class SpeechFilter(private val speeches: List<Speech>) {

    val SPEECH_YEAR = 2013
    val SPEECH_TOPIC = "homeland security"

    fun filter(): EvaluationResponse {
        return EvaluationResponse(
            mostSpeeches = findMostSpeechSpeakers(),
            mostSecurity =  findHomelandSecuritySpeakers(),
            leastWordy = findLeastWordySpeakers()
        );
    }

    fun findMostSpeechSpeakers() : String? {
        // we need to sum year=2013 speeches for each speaker
        val year2013Speeches = speeches
            .filter { it.date.year == SPEECH_YEAR }
            .groupBy { it.speaker }
            .mapValues { entry -> entry.value.sumOf { it.words } }

        val maxWords = year2013Speeches.values.maxOrNull()

        return year2013Speeches.joinKeysWhenValuesEqualTo(maxWords)
    }
    fun findHomelandSecuritySpeakers() : String? {
        // we need to sum "homeland security" speeches for each speaker, ignoring year
        val homelandSecuritySpeeches = speeches
            .filter { it.topic == SPEECH_TOPIC }
            .groupBy { it.speaker }
            .mapValues { entry -> entry.value.sumOf { it.words } }

        val maxWords = homelandSecuritySpeeches.values.maxOrNull()

        return homelandSecuritySpeeches.joinKeysWhenValuesEqualTo(maxWords)
    }

    fun findLeastWordySpeakers() : String? {
        // We need to sum all speeches for each speaker
        val speakerTotalWords = speeches
            .groupBy { it.speaker }
            .mapValues { entry -> entry.value.sumOf { it.words } }

        val minWords = speakerTotalWords.values.minOrNull()

        return speakerTotalWords.joinKeysWhenValuesEqualTo(minWords)
    }
}

