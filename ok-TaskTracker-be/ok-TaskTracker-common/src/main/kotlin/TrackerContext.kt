import kotlinx.datetime.Instant
import models.*
import stubs.TrackerStubs

data class TrackerContext(
    var command: TrackerCommand = TrackerCommand.NONE,
    var state: TrackerState = TrackerState.NONE,
    val errors: MutableList<TrackerError> = mutableListOf(),

    var workMode: TrackerWorkMode = TrackerWorkMode.PROD,
    var stubCase: TrackerStubs = TrackerStubs.NONE,

    var requestId: TrackerRequestId = TrackerRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: TrackerTask = TrackerTask(),
    var taskFilterRequest: TrackerTaskFilter = TrackerTaskFilter(),

    var taskResponse: TrackerTask = TrackerTask(),
    var tasksResponse: MutableList<TrackerTask> = mutableListOf(),
    )

