package com.boswelja.truemanager.core.logviewer.parser

/**
 * Provides functions for sanitizing and extracting data from lines of text that are (usually)
 * written to a log output. See [parseLine] for details.
 */
interface LogParser {

    /**
     * Takes a single line of text and attempts to extract log-related information from it.
     *
     * @return The [LogLine] containing log-related information, as well as the sanitized log
     * contents. If the input line is blank, this returns null.
     */
    fun parseLine(logLine: String): LogLine?

    /**
     * A convenience function that parses multiple lines at once. Blank lines are ignored.
     */
    fun parseLines(logLines: List<String>): List<LogLine> {
        return logLines.mapNotNull { parseLine(it) }
    }
}
