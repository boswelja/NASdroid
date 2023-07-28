package com.boswelja.truemanager.core.logviewer.parser

import kotlinx.datetime.Instant

/**
 * Describes an entry in a log file.
 *
 * @property level The [LogLevel] of the log entry.
 * @property timestamp The precise time at which the entry was logged.
 * @property content The log content.
 */
data class LogLine(
    val level: LogLevel?,
    val timestamp: Instant,
    val content: String
)

/**
 * Log entries (usually) come in "levels". Levels define the severity of the event that was recorded.
 * Typical log entry levels are in the following order:
 * [Debug] - An event that was logged purely for debugging.
 * [Info] - An event that was logged to communicate important information. This is the lowest
 * severity level.
 * [Warning] - An event that may be abnormal. This is the next severity level above info.
 * [Error] - A critical event, possibly fatal. This is the highest severity level.
 */
enum class LogLevel(internal val knownNames: Set<String>, internal val identifier: Char) {
    Debug(setOf("[debug]", "[Debug]", "debug:", "DEBUG", "(D)"), 'D'),
    Info(setOf("[info]", "[Info]", "info:", "INFO", "(I)", "[INF]"), 'I'),
    Warning(setOf("[warn]", "[Warn]", "[warning]", "warn:", "WARNING", "[WRN]", "(W)"), 'W'),
    Error(setOf("[error]", "[Error]", "error:", "ERROR", "[ERR]", "(E)"), 'E'),
}
