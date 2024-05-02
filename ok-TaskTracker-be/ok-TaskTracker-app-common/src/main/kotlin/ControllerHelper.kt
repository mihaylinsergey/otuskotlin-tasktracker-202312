package ru.otus.otuskotlin.marketplace.app.common

import TrackerContext
import kotlinx.datetime.Clock
import models.TrackerCommand
import models.TrackerState
import ru.otus.otuskotlin.marketplace.common.helpers.asTrackerError
import toLog
import kotlin.reflect.KClass

suspend inline fun <T> ITrackerAppSettings.controllerHelper(
    crossinline getRequest: suspend TrackerContext.() -> Unit,
    crossinline toResponse: suspend TrackerContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = TrackerContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = TrackerState.FAILING
        ctx.errors.add(e.asTrackerError())
        processor.exec(ctx)
        if (ctx.command == TrackerCommand.NONE) {
            ctx.command = TrackerCommand.READ
        }
        ctx.toResponse()
    }
}
