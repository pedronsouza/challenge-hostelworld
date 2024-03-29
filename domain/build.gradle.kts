@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.kotlin.coroutines.core)
    testImplementation(libs.bundles.unit.test)
}