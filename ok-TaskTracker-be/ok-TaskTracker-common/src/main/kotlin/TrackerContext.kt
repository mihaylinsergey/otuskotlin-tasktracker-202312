import kotlinx.datetime.Instant
import models.*
import permissions.TrackerPrincipalModel
import permissions.TrackerUserPermissions
import repo.IRepoTask
import stubs.TrackerStubs

data class TrackerContext(
    var command: TrackerCommand = TrackerCommand.NONE,
    var state: TrackerState = TrackerState.NONE,
    val errors: MutableList<TrackerError> = mutableListOf(),

    var corSettings: TrackerCorSettings = TrackerCorSettings(),
    var workMode: TrackerWorkMode = TrackerWorkMode.PROD,
    var stubCase: TrackerStubs = TrackerStubs.NONE,

    var principal: TrackerPrincipalModel = TrackerPrincipalModel.NONE,
    val permissionsChain: MutableSet<TrackerUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: TrackerRequestId = TrackerRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: TrackerTask = TrackerTask(),
    var taskFilterRequest: TrackerTaskFilter = TrackerTaskFilter(),

    var taskValidating: TrackerTask = TrackerTask(),
    var taskFilterValidating: TrackerTaskFilter = TrackerTaskFilter(),

    var taskValidated: TrackerTask = TrackerTask(),
    var taskFilterValidated: TrackerTaskFilter = TrackerTaskFilter(),

    var taskRepo: IRepoTask = IRepoTask.NONE,
    var taskRepoRead: TrackerTask= TrackerTask(), // То, что прочитали из репозитория
    var taskRepoPrepare: TrackerTask = TrackerTask(), // То, что готовим для сохранения в БД
    var taskRepoDone: TrackerTask = TrackerTask(),  // Результат, полученный из БД
    var tasksRepoDone: MutableList<TrackerTask> = mutableListOf(),

    var taskResponse: TrackerTask = TrackerTask(),
    var tasksResponse: MutableList<TrackerTask> = mutableListOf(),
    )

