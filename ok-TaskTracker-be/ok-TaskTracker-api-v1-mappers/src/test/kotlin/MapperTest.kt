import models.*
import org.junit.Test
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import stubs.*
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = TaskCreateRequest(
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = TaskCreateObject(
                title = "task title",
                executor = "task executor",
                stageList = listOf(StageList(
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
                    )),
                deadline = "01.01.2025",
                address = "some address",
                comment = "task comment",
                photo = "https://someurl",
                taskType = WorkSide.CUSTOMER,
                visibility = TaskVisibility.PUBLIC,
            ),
        )

        val context = TrackerContext()
        context.fromTransport(req)

        assertEquals(TrackerStubs.SUCCESS, context.stubCase)
        assertEquals(TrackerWorkMode.STUB, context.workMode)
        assertEquals("task title", context.taskRequest.title)
        assertEquals(TrackerVisibility.VISIBLE_PUBLIC, context.taskRequest.visibility)
        assertEquals(TrackerWorkSide.CUSTOMER, context.taskRequest.taskType)
    }

    @Test
    fun toTransport() {
        val context = TrackerContext(
            requestId = TrackerRequestId("1234"),
            command = TrackerCommand.CREATE,
            taskResponse = TrackerTask(
                title = "task title",
                executor = "task executor",
                stageList = listOf(
                    TrackerStageList(
                    id = "1",
                    stage = "stage one",
                    value = "1.1",
                    comment = "comment one"
                ),
                    TrackerStageList(
                        id = "2",
                        stage = "stage two",
                        value = "2.1",
                        comment = "comment two"
                    )),
                deadline = "01.01.2025",
                address = "some address",
                comment = "task comment",
                photo = "https://someurl",
                taskType = TrackerWorkSide.CUSTOMER,
                visibility = TrackerVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                TrackerError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = TrackerState.RUNNING,
        )

        val req = context.toTransportTask() as TaskCreateResponse

        assertEquals("task title", req.task?.title)
        assertEquals("task executor", req.task?.executor)
        assertEquals("task executor", req.task?.executor)
        assertEquals(listOf(StageList(
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
            )), req.task?.stageList)
        assertEquals("01.01.2025", req.task?.deadline)
        assertEquals("some address", req.task?.address)
        assertEquals("task comment", req.task?.comment)
        assertEquals("https://someurl", req.task?.photo)
        assertEquals(WorkSide.CUSTOMER, req.task?.taskType)
        assertEquals(TaskVisibility.PUBLIC, req.task?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
