package org.political.speeches.extension

fun Map<String, Int>.joinKeysWhenValuesEqualTo(wordCount : Int?): String? {
    // Edge case but speech word counts can be same, joining speakers with comma
    // TODO: It was not clear in the requirement, we need to discuss
    if (wordCount == null) {
        return null
    }
    return filter { it.value == wordCount }
        .keys
        .joinToString(", ")
}