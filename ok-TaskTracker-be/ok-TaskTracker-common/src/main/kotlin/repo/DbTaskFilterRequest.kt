package repo

import models.TrackerUserId
import models.TrackerWorkSide

data class DbTaskFilterRequest(
    val titleFilter: String = "",
    val customerId: TrackerUserId = TrackerUserId.NONE,
    val workSide: TrackerWorkSide = TrackerWorkSide.NONE,
)
