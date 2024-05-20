plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))

    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-common")

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
}
