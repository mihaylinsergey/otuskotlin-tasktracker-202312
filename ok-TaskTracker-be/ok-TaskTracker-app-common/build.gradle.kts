plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)

    // transport models
    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-api-log1"))
    implementation(project(":ok-TaskTracker-biz"))

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))

    testImplementation(libs.coroutines.test)
    testImplementation(project(":ok-TaskTracker-api-log1"))
    testImplementation(project(":ok-TaskTracker-api-v1-jackson"))
    testImplementation(project(":ok-TaskTracker-api-v1-mappers"))
    testImplementation(project(":ok-TaskTracker-app-common"))
    testImplementation(kotlin("test-junit"))
}
