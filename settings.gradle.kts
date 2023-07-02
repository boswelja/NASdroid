import com.android.build.api.dsl.SettingsExtension
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
    id("com.android.settings") version("8.2.0-alpha10")
}

enableFeaturePreview(FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.name)

rootProject.name = "TrueManager"
include(
    ":core:api",
    ":core:capacity",
    ":features:apps",
    ":features:auth",
    ":features:dashboard:data",
    ":features:dashboard:logic",
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

configure<SettingsExtension> {
    compileSdk = 33
    minSdk = 28
}
