package repo

import TrackerContext
import exceptions.TrackerTaskDbNotConfiguredException
import helpers.errorSystem
import helpers.fail
import models.TrackerWorkMode
import permissions.TrackerUserGroups
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        taskRepo = when {
            workMode == TrackerWorkMode.TEST -> corSettings.repoTest
            workMode == TrackerWorkMode.STUB -> corSettings.repoStub
            principal.groups.contains(TrackerUserGroups.TEST) -> corSettings.repoTest
            else -> corSettings.repoProd
        }
        if (workMode != TrackerWorkMode.STUB && taskRepo == IRepoTask.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = TrackerTaskDbNotConfiguredException(workMode)
            )
        )
    }
}
