import models.TrackerTask
import models.TrackerUserId
import models.TrackerWorkSide
import repo.DbTaskFilterRequest
import repo.DbTasksResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskSearchTest {
    abstract val repo: TaskRepoInitialized

    protected open val initializedObjects: List<TrackerTask> = initObjects

    @Test
    fun searchCustomer() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(customerId = searchOwnerId))
        assertIs<DbTasksResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

//    @Test
//    fun searchDealSide() = runRepoTest {
//        val result = repo.searchTask(DbTaskFilterRequest(workSide = TrackerWorkSide.CUSTOMER))
//        assertIs<DbTasksResponseOk>(result)
//        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
//        assertEquals(expected, result.data.sortedBy { it.id.asString() })
//    }

    companion object: BaseInitTasks("search") {
        val searchOwnerId = TrackerUserId("owner-124")
        override val initObjects: List<TrackerTask> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", customerId = searchOwnerId),
            createInitTestModel("ad3", taskType = TrackerWorkSide.CUSTOMER),
            createInitTestModel("ad4", customerId = searchOwnerId),
            createInitTestModel("ad5", taskType = TrackerWorkSide.CUSTOMER),
        )
    }
}

