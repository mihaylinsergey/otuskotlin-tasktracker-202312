import repo.IRepoTask
import ru.otus.otuskotlin.TaskTracker.logging.common.TrackerLoggerProvider

data class TrackerCorSettings(
    val loggerProvider: TrackerLoggerProvider = TrackerLoggerProvider(),
    val repoStub: IRepoTask = IRepoTask.NONE,
    val repoTest: IRepoTask = IRepoTask.NONE,
    val repoProd: IRepoTask = IRepoTask.NONE,
) {
    companion object {
        val NONE = TrackerCorSettings()
    }
}
