package ru.otus.otuskotlin.TaskTracker.biz

import TrackerContext
import TrackerCorSettings
import models.TrackerState
import models.TrackerWorkSide

@Suppress("unused", "RedundantSuspendModifier")
class TrackerTaskProcessor(val corSettings: TrackerCorSettings) {
    suspend fun exec(ctx: TrackerContext) {
        ctx.taskResponse = TrackerTaskStub.get()
        ctx.tasksResponse = TrackerTaskStub.prepareSearchList("ad search", TrackerWorkSide.CUSTOMER).toMutableList()
        ctx.state = TrackerState.RUNNING
    }
}
