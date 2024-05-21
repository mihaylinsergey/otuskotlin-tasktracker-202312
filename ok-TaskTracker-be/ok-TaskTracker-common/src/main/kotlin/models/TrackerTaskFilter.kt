package models

data class TrackerTaskFilter(
    var searchString: String = "",
    var customerId: TrackerUserId = TrackerUserId.NONE,
    var workSide: TrackerWorkSide = TrackerWorkSide.NONE,
) {
    fun deepCopy(): TrackerTaskFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = TrackerTaskFilter()
    }
}
