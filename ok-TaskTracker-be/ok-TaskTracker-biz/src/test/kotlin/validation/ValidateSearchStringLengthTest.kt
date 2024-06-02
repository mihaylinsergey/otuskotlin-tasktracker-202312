package validation

import TrackerContext
import models.TrackerState
import models.TrackerTaskFilter
import ru.otus.otuskotlin.TrackerTask.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(TrackerState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(TrackerState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(TrackerState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(TrackerState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(TrackerState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
