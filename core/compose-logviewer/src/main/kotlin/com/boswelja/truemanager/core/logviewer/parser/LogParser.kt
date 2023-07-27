package com.boswelja.truemanager.core.logviewer.parser

interface LogParser {

    fun parseLine(logLine: String): LogLine
}
