plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))

    implementation(libs.kotlinx.datetime)
    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-common")
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
}
