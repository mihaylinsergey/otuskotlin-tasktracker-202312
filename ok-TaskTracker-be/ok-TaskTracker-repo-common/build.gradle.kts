plugins {
    id("build-jvm")
}

dependencies {
    implementation(project(":ok-TaskTracker-common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
}
