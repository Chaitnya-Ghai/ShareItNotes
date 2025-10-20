pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "ShareItNotes"
include(":app")
include(":notes:ui")
include(":notes:data")
include(":notes:domain")
include(":auth:data")
include(":auth:ui")
include(":auth:domain")
include(":shared-notes:data")
include(":shared-notes:ui")
include(":shared-notes:domain")
