package repo

import TrackerContext
import models.TrackerState
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != TrackerWorkMode.STUB }
    handle {
        taskResponse = taskRepoDone
        tasksResponse = tasksRepoDone
        state = when (val st = state) {
            TrackerState.RUNNING -> TrackerState.FINISHING
            else -> st
        }
    }
}
