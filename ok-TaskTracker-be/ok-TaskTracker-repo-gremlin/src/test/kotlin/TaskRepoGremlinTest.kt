import models.TrackerTask
import models.TrackerTaskId

class RepoAdGremlinCreateTest : RepoTaskCreateTest() {
    override val repo = TaskRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_create", lockNew.asString())
    )
}

class RepoAdGremlinReadTest : RepoTaskReadTest() {
    override val repo = TaskRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_read")
    )
     override val readSucc = repo.initializedObjects[0]
}

class RepoAdGremlinUpdateTest : RepoTaskUpdateTest() {
    override val repo = TaskRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_update", lockNew.asString())
    )
    override val updateSucc: TrackerTask by lazy { repo.initializedObjects[0] }
    override val updateConc: TrackerTask by lazy { repo.initializedObjects[1] }
}

class RepoAdGremlinDeleteTest : RepoTaskDeleteTest() {
    override val repo = TaskRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_delete")
    )
    override val deleteSucc: TrackerTask by lazy { repo.initializedObjects[0] }
    override val deleteConc: TrackerTask by lazy { repo.initializedObjects[1] }
    override val notFoundId: TrackerTaskId = TrackerTaskId("#3100:0")
}

class RepoAdGremlinSearchTest : RepoTaskSearchTest() {
    override val repo = TaskRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_search")
    )
    override val initializedObjects: List<TrackerTask> = repo.initializedObjects
}
