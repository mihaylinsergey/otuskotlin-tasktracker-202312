plugins {
    id("build-jvm")
}
dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(libs.coroutines.core)
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(libs.coroutines.test)
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}
