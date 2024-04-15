package models

data class TrackerTaskFilter(
    var searchString: String = "",
    var ownerId: TrackerUserId = TrackerUserId.NONE,
    var workSide: TrackerWorkSide = TrackerWorkSide.NONE,
)
