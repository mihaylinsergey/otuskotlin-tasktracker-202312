package validation

import models.TrackerCommand
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleCorrect
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleEmpty
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleSymbols
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleTrim
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = TrackerCommand.UPDATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationExecutorCorrect(command, processor)
    @Test fun trimDescription() = validationExecutorTrim(command, processor)
    @Test fun emptyDescription() = validationExecutorEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationExecutorSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
