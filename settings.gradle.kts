pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://repo.eclipse.org/content/groups/releases/") }
        mavenCentral()
        google()
    }
}

rootProject.name = "GitHub Client"

include(":library")

/* JitPack: exclude module `:mobile` */
if (System.getenv("JITPACK") == null) {
    include(":mobile")
}
