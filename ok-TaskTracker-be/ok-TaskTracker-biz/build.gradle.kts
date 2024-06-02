plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))

    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-stubs"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.cor)

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    api(libs.coroutines.test)
    testImplementation(kotlin("test-junit"))
}
