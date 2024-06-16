package repo

import TrackerContext
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == TrackerState.RUNNING }
    handle {
        taskRepoPrepare = taskValidated.deepCopy()
    }
}
