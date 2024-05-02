package models

data class TrackerTaskFilter(
    var searchString: String = "",
    var customerId: TrackerUserId = TrackerUserId.NONE,
    var workSide: TrackerWorkSide = TrackerWorkSide.NONE,
)
