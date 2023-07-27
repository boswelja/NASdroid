package com.boswelja.truemanager.core.logviewer.parser

import kotlinx.datetime.Instant

data class LogLine(
    val level: LogLevel?,
    val timestamp: Instant,
    val content: String
)

enum class LogLevel(internal val knownNames: Set<String>) {
    Debug(setOf("debug")),
    Info(setOf("info")),
    Warning(setOf("warn", "warning")),
    Error(setOf("error")),
}
