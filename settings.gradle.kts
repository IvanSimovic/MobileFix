rootProject.name = "MobileFix"
include(":app")

pluginManagement {
    includeBuild("buildLogic")
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
        // Added for testing local Konsist artifacts
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// Generate type safe accessors when referring to other projects eg.
// Before: implementation(project(":feature_album"))
// After: implementation(projects.featureAlbum)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":feature:base")
include(":feature:album")
include(":feature:favourite")
include(":feature:settings")
include(":library:testUtils")
