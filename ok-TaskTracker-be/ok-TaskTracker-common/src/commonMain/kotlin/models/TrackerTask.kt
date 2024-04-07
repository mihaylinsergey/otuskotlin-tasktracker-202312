package models

data class TrackerTask(
    var id: TrackerTaskId = TrackerTaskId.NONE,
    var title: String = "",
    var executor: String = "",
    var stageList: List<TrackerStageList> = mutableListOf(),
    var deadline: String = "",
    var address: String = "",
    var comment: String = "",
    var photo: String = "",
    var customerId: TrackerUserId = TrackerUserId.NONE,
    var taskType: TrackerWorkSide = TrackerWorkSide.NONE,
    var visibility: TrackerVisibility = TrackerVisibility.NONE,
    var lock: TrackerTaskLock = TrackerTaskLock.NONE,
    val permissionsClient: MutableSet<TrackerTaskPermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = TrackerTask()
    }

}
