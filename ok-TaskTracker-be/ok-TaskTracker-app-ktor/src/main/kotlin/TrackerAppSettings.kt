import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import ru.otus.otuskotlin.marketplace.app.common.ITrackerAppSettings

data class TrackerAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: TrackerCorSettings = TrackerCorSettings(),
    override val processor: TrackerTaskProcessor = TrackerTaskProcessor(corSettings),
): ITrackerAppSettings
