import kotlinx.coroutines.test.runTest
import models.TrackerTask
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TaskRepositoryMockTest {
    private val repo = TaskRepositoryMock(
        invokeCreateTask = { DbTaskResponseOk(TrackerTaskStub.prepareResult { title = "create" }) },
        invokeReadTask = { DbTaskResponseOk(TrackerTaskStub.prepareResult { title = "read" }) },
        invokeUpdateTask = { DbTaskResponseOk(TrackerTaskStub.prepareResult { title = "update" }) },
        invokeDeleteTask = { DbTaskResponseOk(TrackerTaskStub.prepareResult { title = "delete" }) },
        invokeSearchTask = { DbTasksResponseOk(listOf(TrackerTaskStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createTask(DbTaskRequest(TrackerTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readTask(DbTaskIdRequest(TrackerTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateTask(DbTaskRequest(TrackerTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteTask(DbTaskIdRequest(TrackerTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchTask(DbTaskFilterRequest())
        assertIs<DbTasksResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }

}
