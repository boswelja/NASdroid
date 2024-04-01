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
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}

plugins {
    id("com.gradle.enterprise") version("3.16")
    id("com.android.settings") version("8.2.0")
}

enableFeaturePreview(FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.name)

rootProject.name = "NASdroid"
include(
    ":core:api",
    ":core:capacity",
    ":core:compose-logviewer",
    ":core:compose-segmentedprogressindicator",
    ":core:design",
    ":core:percentage",
    ":core:strong-result",
    ":core:temperature",
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

configure<SettingsExtension> {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    minSdk = 28
}
