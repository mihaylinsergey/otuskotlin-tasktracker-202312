package ru.otus.otuskotlin.marketplace.app.common

import TrackerCorSettings
import fromTransport
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import ru.otus.otuskotlin.TaskTracker.biz.TrackerTaskProcessor
import toTransportTask
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV1Test {

    private val request = TaskCreateRequest(
        task = TaskCreateObject(
            title = "task title",
            executor = "task executor",
            stageList = listOf(
                StageList(
                id = "1",
                stage = "stage one",
                value = "1.1",
                comment = "comment one"
            ),
                StageList(
                    id = "2",
                    stage = "stage two",
                    value = "2.1",
                    comment = "comment two"
                )
            ),
            deadline = "01.01.2025",
            address = "some address",
            comment = "task comment",
            photo = "https://someurl",
            taskType = WorkSide.CUSTOMER,
            visibility = TaskVisibility.PUBLIC,
        ),
        debug = TaskDebug(
            mode = TaskRequestDebugMode.STUB,
            stub = TaskRequestDebugStubs.SUCCESS,
        ),
    )

    private val appSettings: ITrackerAppSettings = object : ITrackerAppSettings {
        override val corSettings: TrackerCorSettings = TrackerCorSettings()
        override val processor: TrackerTaskProcessor = TrackerTaskProcessor(corSettings)
    }

    private suspend fun createTaskSpring(request: TaskCreateRequest): TaskCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportTask() as TaskCreateResponse },
            ControllerV1Test::class,
            "controller-v1-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createTaskKtor(appSettings: ITrackerAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TaskCreateRequest>()) },
            { toTransportTask() },
            ControllerV1Test::class,
            "controller-v1-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createTaskSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createTaskKtor(appSettings) }
        val res = testApp.res as TaskCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
