package ru.otus.otuskotlin.marketplace.biz.general

import TrackerContext
import models.TrackerCommand
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.chain

fun ICorChainDsl<TrackerContext>.operation(
    title: String,
    command: TrackerCommand,
    block: ICorChainDsl<TrackerContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == TrackerState.RUNNING }
}
