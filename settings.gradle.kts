pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "hostelworld-challenge"
include(":app")
include(":features:property_list")
include(":shared")
include(":domain")
include(":data")
include(":features:property_detail")
include(":shared-test")
