package ru.otus.otuskotlin.marketplace.app.common

import TrackerCorSettings
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor


interface ITrackerAppSettings {
    val processor: TrackerTaskProcessor
    val corSettings: TrackerCorSettings
}
