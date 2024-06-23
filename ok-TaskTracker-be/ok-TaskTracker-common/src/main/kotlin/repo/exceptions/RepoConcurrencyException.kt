package repo.exceptions

import models.TrackerTaskId
import models.TrackerTaskLock

class RepoConcurrencyException(id: TrackerTaskId, expectedLock: TrackerTaskLock, actualLock: TrackerTaskLock?): RepoTaskException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
