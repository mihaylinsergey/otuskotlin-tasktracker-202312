package ru.otus.otuskotlin.TaskTracker.app.ktor.stub

import TrackerAppSettings
import TrackerCorSettings
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import ru.otus.otuskotlin.TaskTracker.app.ktor.moduleJvm
import kotlin.test.Test
import kotlin.test.assertEquals

class V1AdStubApiTest {
    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = TaskCreateRequest(
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
        ),
    ) { response ->
        val responseObj = response.body<TaskCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.task?.id)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = TaskReadRequest(
            task = TaskReadObject("666"),
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<TaskReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.task?.id)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = TaskUpdateRequest(
            task = TaskUpdateObject(
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
            )
        ),
    ) { response ->
        val responseObj = response.body<TaskUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.task?.id)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = TaskDeleteRequest(
            task = TaskDeleteObject(
                id = "123",
            ),
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<TaskDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("123", responseObj.task?.id)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = TaskSearchRequest(
            taskFilter = TaskSearchFilter(),
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            )
        ),
    ) { response ->
        val responseObj = response.body<TaskSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.tasks?.first()?.id)
    }


    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(TrackerAppSettings(corSettings = TrackerCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/task/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
