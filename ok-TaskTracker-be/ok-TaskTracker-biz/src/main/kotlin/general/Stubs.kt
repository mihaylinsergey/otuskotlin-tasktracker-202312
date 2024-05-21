package ru.otus.otuskotlin.marketplace.biz.general

import TrackerContext
import models.TrackerState
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.chain

fun ICorChainDsl<TrackerContext>.stubs(title: String, block: ICorChainDsl<TrackerContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == TrackerWorkMode.STUB && state == TrackerState.RUNNING }
}
