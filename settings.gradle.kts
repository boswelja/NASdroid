import org.gradle.api.internal.FeaturePreviews

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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}

plugins {
    id("com.gradle.enterprise") version("3.16")
    id("com.android.settings") version("8.5.1")
}

enableFeaturePreview(FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.name)

rootProject.name = "NASdroid"
include(
    ":core:api",
    ":core:compose-logviewer",
    ":core:compose-segmentedprogressindicator",
    ":core:design",
    ":core:navigation",
    ":core:skeleton",
    ":core:strong-result",
    ":features:apps:data",
    ":features:apps:logic",
    ":features:apps:ui",
    ":features:auth:data",
    ":features:auth:logic",
    ":features:auth:ui",
    ":features:credentials:logic",
    ":features:credentials:ui",
    ":features:dashboard:data",
    ":features:dashboard:logic",
    ":features:dashboard:ui",
    ":features:dataprotection:logic",
    ":features:dataprotection:ui",
    ":features:datasets:logic",
    ":features:datasets:ui",
    ":features:network:logic",
    ":features:network:ui",
    ":features:power:logic",
    ":features:power:ui",
    ":features:reporting:data",
    ":features:reporting:logic",
    ":features:reporting:ui",
    ":features:shares:logic",
    ":features:shares:ui",
    ":features:storage:logic",
    ":features:storage:ui",
    ":features:systemsettings:logic",
    ":features:systemsettings:ui",
    ":features:virtualization:logic",
    ":features:virtualization:ui",
    ":app"
)

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        if (System.getenv("CI") == "true") {
            termsOfServiceAgree = "yes"
            isUploadInBackground = false
        }
    }
}

android {
    buildToolsVersion = "35.0.0"
    compileSdk = 35
    minSdk = 28
}
