import org.gradle.api.internal.FeaturePreviews

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

plugins {
    id("com.gradle.enterprise") version("3.13")
}

enableFeaturePreview(FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.name)

rootProject.name = "TrueManager"
include(
    ":core:api",
    ":core:capacity",
    ":features:apps",
    ":features:auth",
    ":features:dashboard:business",
    ":features:dashboard:data",
    ":features:dashboard:ui",
    ":features:reporting",
    ":features:storage",
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
