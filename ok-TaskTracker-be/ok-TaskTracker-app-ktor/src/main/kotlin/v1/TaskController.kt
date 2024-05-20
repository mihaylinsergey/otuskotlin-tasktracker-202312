package ru.otus.otuskotlin.marketplace.app.ktor.v1

import TrackerAppSettings
import io.ktor.server.application.*
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import ru.otus.otuskotlin.TrackerTask.app.ktor.v1.processV1
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTask::class
suspend fun ApplicationCall.createTask(appSettings: TrackerAppSettings) =
    processV1<TaskCreateRequest, TaskCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readTask::class
suspend fun ApplicationCall.readTask(appSettings: TrackerAppSettings) =
    processV1<TaskReadRequest, TaskReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateTask::class
suspend fun ApplicationCall.updateTask(appSettings: TrackerAppSettings) =
    processV1<TaskUpdateRequest, TaskUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteTask::class
suspend fun ApplicationCall.deleteTask(appSettings: TrackerAppSettings) =
    processV1<TaskDeleteRequest, TaskDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchTask::class
suspend fun ApplicationCall.searchTask(appSettings: TrackerAppSettings) =
    processV1<TaskSearchRequest, TaskSearchResponse>(appSettings, clSearch, "search")
