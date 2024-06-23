package exceptions

import models.TrackerCommand

class UnknownTrackerCommand(command: TrackerCommand) : Throwable("Wrong command $command at mapping toTransport stage")

