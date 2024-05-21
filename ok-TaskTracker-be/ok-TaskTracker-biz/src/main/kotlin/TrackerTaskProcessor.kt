package ru.otus.otuskotlin.TrackerTask.biz

import TrackerContext
import TrackerCorSettings
import models.TrackerCommand
import models.TrackerTaskId
import models.TrackerTaskLock
import ru.otus.otuskotlin.TrackerTask.cor.rootChain
import ru.otus.otuskotlin.TrackerTask.cor.worker
import ru.otus.otuskotlin.marketplace.biz.general.initStatus
import ru.otus.otuskotlin.marketplace.biz.general.operation
import ru.otus.otuskotlin.marketplace.biz.general.stubs
import stubs.*
import validation.*

class TrackerTaskProcessor(
    private val corSettings: TrackerCorSettings = TrackerCorSettings.NONE
) {
    suspend fun exec(ctx: TrackerContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<TrackerContext> {
        initStatus("Инициализация статуса")
//        initRepo("Инициализация репозитория")

        operation("Создание задачи", TrackerCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadExecutor("Имитация ошибки валидации исполнителя")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = TrackerTaskId.NONE }
                worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                worker("Очистка поля исполнитель") { taskValidating.executor = taskValidating.executor.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateExecutorNotEmpty("Проверка, что поле исполнитель не пусто")
                validateExecutorHasContent("Проверка символов")

                finishAdValidation("Завершение проверок")
            }
//            chain {
//                title = "Логика сохранения"
//                repoPrepareCreate("Подготовка объекта для сохранения")
//                repoCreate("Создание объявления в БД")
//            }
//            prepareResult("Подготовка ответа")
        }
        operation("Получить объявление", TrackerCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = TrackerTaskId(taskValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishAdValidation("Успешное завершение процедуры валидации")
            }
//            chain {
//                title = "Логика чтения"
//                repoRead("Чтение объявления из БД")
//                worker {
//                    title = "Подготовка ответа для Read"
//                    on { state == TrackerState.RUNNING }
//                    handle { taskRepoDone = taskRepoRead }
//                }
//            }
//            prepareResult("Подготовка ответа")
        }
        operation("Изменить объявление", TrackerCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadExecutor("Имитация ошибки валидации исполнителя")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = TrackerTaskId(taskValidating.id.asString().trim()) }
                worker("Очистка lock") { taskValidating.lock = TrackerTaskLock(taskValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                worker("Очистка исполнителя") { taskValidating.executor = taskValidating.executor.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateExecutorNotEmpty("Проверка на непустого исполнителя")
                validateExecutorHasContent("Проверка на наличие содержания в поле исполнитель")

                finishAdValidation("Успешное завершение процедуры валидации")
            }
//            chain {
//                title = "Логика сохранения"
//                repoRead("Чтение объявления из БД")
//                checkLock("Проверяем консистентность по оптимистичной блокировке")
//                repoPrepareUpdate("Подготовка объекта для обновления")
//                repoUpdate("Обновление объявления в БД")
//            }
//            prepareResult("Подготовка ответа")
        }
        operation("Удалить объявление", TrackerCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в taskValidating") {
                    taskValidating = taskRequest.deepCopy()
                }
                worker("Очистка id") { taskValidating.id = TrackerTaskId(taskValidating.id.asString().trim()) }
                worker("Очистка lock") { taskValidating.lock = TrackerTaskLock(taskValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishAdValidation("Успешное завершение процедуры валидации")
            }
//            chain {
//                title = "Логика удаления"
//                repoRead("Чтение объявления из БД")
//                checkLock("Проверяем консистентность по оптимистичной блокировке")
//                repoPrepareDelete("Подготовка объекта для удаления")
//                repoDelete("Удаление объявления из БД")
//            }
//            prepareResult("Подготовка ответа")
        }
        operation("Поиск объявлений", TrackerCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adFilterValidating") { taskFilterValidating = taskFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
//            repoSearch("Поиск объявления в БД по фильтру")
//            prepareResult("Подготовка ответа")
        }
    }.build()
}

