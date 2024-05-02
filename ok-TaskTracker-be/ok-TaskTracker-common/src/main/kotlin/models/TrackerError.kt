package models

import ru.otus.otuskotlin.TaskTracker.logging.common.LogLevel

data class TrackerError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
