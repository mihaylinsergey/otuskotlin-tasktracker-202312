package repo

import TrackerContext
import models.*
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == TrackerState.RUNNING }
    handle {
        taskRepoPrepare = taskRepoRead.deepCopy().apply {
            this.title = taskValidated.title
            this.executor = taskValidated.executor
            this.stageList = taskValidated.stageList
            this.deadline = taskValidated.deadline
            this.address = taskValidated.address
            this.comment = taskValidated.comment
            this.taskType = taskValidated.taskType
            this.visibility= taskValidated.visibility
            this.lock = taskValidated.lock
        }
    }
}
