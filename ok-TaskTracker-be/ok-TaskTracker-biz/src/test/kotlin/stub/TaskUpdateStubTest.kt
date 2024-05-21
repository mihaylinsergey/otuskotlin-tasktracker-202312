package stub

import TrackerContext
import kotlinx.coroutines.test.runTest
import models.*
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import stubs.TrackerStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskUpdateStubTest {

    private val processor = TrackerTaskProcessor()
    val id = TrackerTaskId("777")
    val title = "title 666"
    val executor = "executor 666"
    val workSide = TrackerWorkSide.CUSTOMER
    val visibility = TrackerVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = TrackerContext(
            command = TrackerCommand.UPDATE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.SUCCESS,
            taskRequest = TrackerTask(
                id = id,
                title = title,
                executor = executor,
                taskType = workSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.taskResponse.id)
        assertEquals(title, ctx.taskResponse.title)
        assertEquals(executor, ctx.taskResponse.executor)
        assertEquals(workSide, ctx.taskResponse.taskType)
        assertEquals(visibility, ctx.taskResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.UPDATE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_TITLE,
            taskRequest = TrackerTask(
                id = id,
                title = "",
                executor = executor,
                taskType = workSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badExecutor() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.UPDATE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_EXECUTOR,
            taskRequest = TrackerTask(
                id = id,
                title = title,
                executor = "",
                taskType = workSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("executor", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.UPDATE,
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
            command = TrackerCommand.UPDATE,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_SEARCH_STRING,
            taskRequest = TrackerTask(
                id = id,
                title = title,
                executor = executor,
                taskType = workSide,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
