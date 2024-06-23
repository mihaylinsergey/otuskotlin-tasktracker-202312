package plugins

import TaskRepoStub
import TrackerAppSettings
import TrackerCorSettings
import io.ktor.server.application.*
import ru.otus.otuskotlin.TaskTracker.app.ktor.plugins.getLoggerProviderConf
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor

fun Application.initAppSettings(): TrackerAppSettings {
    val corSettings = TrackerCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(TaskDbType.TEST),
        repoProd = getDatabaseConf(TaskDbType.PROD),
        repoStub = TaskRepoStub(),
    )
    return TrackerAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = TrackerTaskProcessor(corSettings),
    )
}
