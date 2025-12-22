package com.tacosforchessur.hardwire.core.logging

import androidx.compose.runtime.mutableStateListOf
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
object Logger {

    val logs = mutableStateListOf<LogEntry>()

    fun i(msg: String) = addLog(msg, LogLevel.INFO)
    fun d(msg: String) = addLog(msg, LogLevel.DEBUG)
    fun e(msg: String) = addLog(msg, LogLevel.ERROR)

    private fun addLog(msg: String, level: LogLevel) {
        val timestamp = getCurrentTimestamp()
        logs.add(LogEntry(msg, level, timestamp))
        if (logs.size > 100) logs.removeAt(0)
    }

    fun clearLogs() {
        logs.clear()
    }

    private fun getCurrentTimestamp(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}:${localDateTime.second.toString().padStart(2, '0')}"
    }

}