package validation

import TrackerContext
import models.TrackerState
import models.TrackerTask
import models.TrackerTaskFilter
import ru.otus.otuskotlin.TrackerTask.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskValidating = TrackerTask(title = ""))
        chain.exec(ctx)
        assertEquals(TrackerState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskValidating = TrackerTask(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(TrackerState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = TrackerContext(state = TrackerState.RUNNING, taskFilterValidating = TrackerTaskFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(TrackerState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
