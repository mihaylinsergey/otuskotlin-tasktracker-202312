plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-TaskTracker-api-v1-jackson"))
    implementation(project(":ok-TaskTracker-common"))

    testImplementation(kotlin("test-junit"))
}
