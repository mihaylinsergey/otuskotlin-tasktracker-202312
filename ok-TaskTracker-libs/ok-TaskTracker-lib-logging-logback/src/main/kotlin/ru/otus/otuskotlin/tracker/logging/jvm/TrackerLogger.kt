package ru.otus.otuskotlin.tracker.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.TaskTracker.logging.common.*
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun trackerLoggerLogback(logger: Logger): ITrLogWrapper = TrackerLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun trackerLoggerLogback(clazz: KClass<*>): ITrLogWrapper = trackerLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun trackerLoggerLogback(loggerId: String): ITrLogWrapper = trackerLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
