package validation

import models.TrackerCommand
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleCorrect
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleEmpty
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleSymbols
import ru.otus.otuskotlin.marketplace.biz.validation.validationTitleTrim
import kotlin.test.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: TrackerCommand = TrackerCommand.CREATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctExecutor() = validationExecutorCorrect(command, processor)
    @Test fun trimExecutor() = validationExecutorTrim(command, processor)
    @Test fun emptyExecutor() = validationExecutorEmpty(command, processor)
    @Test fun badSymbolsExecutor() = validationExecutorSymbols(command, processor)
}
