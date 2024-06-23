class TaskRepoInMemoryCreateTest : RepoTaskCreateTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryDeleteTest : RepoTaskDeleteTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryReadTest : RepoTaskReadTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemorySearchTest : RepoTaskSearchTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryUpdateTest : RepoTaskUpdateTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
