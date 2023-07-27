package com.boswelja.truemanager.core.logviewer

import kotlinx.datetime.Instant

data class LogLine(
    val timestamp: Instant,
    val content: String
) {
    companion object {

        private val timestampRegex = Regex("^(\\d{4})-(\\d{2})-(\\d{2})[T ](\\d{2}):(\\d{2}):(\\d{2}(?:\\.\\d*)?)((\\+(\\d{2}):(\\d{2})|Z)?)((-(\\d{2}):(\\d{2})|Z)?)")
        fun from(logLine: String): LogLine {
            val timestampMatch = timestampRegex.find(logLine)
            requireNotNull(timestampMatch) { "Log line with no timestamp detected!" }
            require(timestampMatch.range.first == 0) { "Did not grab the correct timestamp!" }
            return LogLine(
                timestamp = Instant.parse(timestampMatch.value),
                content = logLine.substring(timestampMatch.range.last)
            )
        }
    }
}
