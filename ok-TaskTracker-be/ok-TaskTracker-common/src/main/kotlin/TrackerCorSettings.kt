import ru.otus.otuskotlin.TaskTracker.logging.common.TrackerLoggerProvider

data class TrackerCorSettings(
    val loggerProvider: TrackerLoggerProvider = TrackerLoggerProvider(),
) {
    companion object {
        val NONE = TrackerCorSettings()
    }
}
