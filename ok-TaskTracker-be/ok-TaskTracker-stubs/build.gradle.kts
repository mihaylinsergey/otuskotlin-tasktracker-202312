plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(project(":ok-TaskTracker-common"))

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
}
