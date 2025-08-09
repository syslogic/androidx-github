pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/syslogic/gpr-maintanance-gradle-plaugin")
            if (System.getenv("GITHUB_ACTOR") != null && System.getenv("GITHUB_ACTOR") != null) {
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
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

rootProject.name = "androidx-github-client"

include(":library")
project(":library").name = "androidx-github"

/* GitHub & JitPack: don't include module `:mobile` */
if (System.getenv("JITPACK") == null && System.getenv("GITHUB_ACTIONS") == null) {
    include(":mobile")
}
