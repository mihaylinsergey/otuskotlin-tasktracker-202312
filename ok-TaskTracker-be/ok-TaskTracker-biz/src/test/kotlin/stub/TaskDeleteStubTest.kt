package stub

import TrackerContext
import kotlinx.coroutines.test.runTest
import models.*
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import stubs.TrackerStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskDeleteStubTest {

    private val processor = TrackerTaskProcessor()
    val id = TrackerTaskId("666")

    @Test
    fun delete() = runTest {

        val ctx = TrackerContext(
            command = TrackerCommand.DELETE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.SUCCESS,
            taskRequest = TrackerTask(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = TrackerTaskStub.get()
        assertEquals(stub.id, ctx.taskResponse.id)
        assertEquals(stub.title, ctx.taskResponse.title)
        assertEquals(stub.executor, ctx.taskResponse.executor)
        assertEquals(stub.taskType, ctx.taskResponse.taskType)
        assertEquals(stub.visibility, ctx.taskResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.DELETE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_ID,
            taskRequest = TrackerTask(),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.DELETE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.DB_ERROR,
            taskRequest = TrackerTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.DELETE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_TITLE,
            taskRequest = TrackerTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
