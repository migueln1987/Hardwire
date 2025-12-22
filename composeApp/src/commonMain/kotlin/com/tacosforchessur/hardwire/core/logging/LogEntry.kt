package com.tacosforchessur.hardwire.core.logging

enum class LogLevel { INFO, DEBUG, ERROR}
data class LogEntry(
    val message: String,
    val level: LogLevel,
    val timeStamp: String,
)
