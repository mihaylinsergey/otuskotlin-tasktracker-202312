package ru.otus.otuskotlin.TaskTracker.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class TrackerLoggerProvider(
    private val provider: (String) -> ITrLogWrapper = { ITrLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}
