package stub

import TrackerContext
import kotlinx.coroutines.test.runTest
import models.*
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import stubs.TrackerStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TaskSearchStubTest {

    private val processor = TrackerTaskProcessor()
    val filter = TrackerTaskFilter(searchString = "task")

    @Test
    fun read() = runTest {

        val ctx = TrackerContext(
            command = TrackerCommand.SEARCH,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.SUCCESS,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.tasksResponse.size > 1)
        val first = ctx.tasksResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        with (TrackerTaskStub.get()) {
            assertEquals(taskType, first.taskType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.SEARCH,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_ID,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.SEARCH,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.DB_ERROR,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = TrackerContext(
            command = TrackerCommand.SEARCH,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.STUB,
            stubCase = TrackerStubs.BAD_TITLE,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackerTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
