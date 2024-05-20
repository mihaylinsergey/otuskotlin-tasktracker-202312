package ru.otus.otuskotlin.TaskTracker.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.tracker.logging.jvm.trackerLoggerLogback
import ru.otus.otuskotlin.TaskTracker.logging.common.TrackerLoggerProvider

fun Application.getLoggerProviderConf(): TrackerLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> TrackerLoggerProvider { trackerLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
}
