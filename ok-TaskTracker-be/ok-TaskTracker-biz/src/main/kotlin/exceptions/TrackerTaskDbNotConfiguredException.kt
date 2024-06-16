package exceptions

import models.TrackerWorkMode

class TrackerTaskDbNotConfiguredException(val workMode: TrackerWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
