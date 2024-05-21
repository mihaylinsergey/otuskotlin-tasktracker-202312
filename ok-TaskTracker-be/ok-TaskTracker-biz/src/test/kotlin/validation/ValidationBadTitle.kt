package ru.otus.otuskotlin.marketplace.biz.validation

import TrackerContext
import models.TrackerCommand
import models.TrackerState
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import validation.runBizTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTitleCorrect(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            title = "abc"
        },
    )
    processor.exec(ctx)
    println(ctx.taskRequest.title)
    println(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.title)
}

fun validationTitleTrim(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            title = " \n\t abc \t\n "
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.title)
}

fun validationTitleEmpty(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            title = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            title = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
