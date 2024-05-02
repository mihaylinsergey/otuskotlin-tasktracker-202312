package ru.otus.otuskotlin.marketplace.app.ktor.v1

import TrackerAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Task(appSettings: TrackerAppSettings) {
    route("task") {
        post("create") {
            call.createTask(appSettings)
        }
        post("read") {
            call.readTask(appSettings)
        }
        post("update") {
            call.updateTask(appSettings)
        }
        post("delete") {
            call.deleteTask(appSettings)
        }
        post("search") {
            call.searchTask(appSettings)
        }
    }
}
