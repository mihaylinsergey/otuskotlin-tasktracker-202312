package mappers

import models.TrackerTask

fun TrackerTask.label(): String? = this::class.simpleName
