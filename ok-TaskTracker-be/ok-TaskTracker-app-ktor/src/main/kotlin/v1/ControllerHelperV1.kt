package ru.otus.otuskotlin.TrackerTask.app.ktor.v1

import TrackerAppSettings
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.TaskTracker.api.v1.models.IRequest
import ru.otus.otuskotlin.TaskTracker.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import toTransportTask
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: TrackerAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    {
        respond(toTransportTask())
    },
    clazz,
    logId,
)
