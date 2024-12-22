plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.dashboard.ui"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        sarifReport = true
        htmlReport = false
    }
}

kotlin {
    jvmToolchain(21)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    implementation(projects.core.design)
    implementation(projects.core.composeSegmentedprogressindicator)
    implementation(projects.core.navigation)
    implementation(projects.core.skeleton)
    implementation(projects.features.dashboard.logic)

    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.menuprovider)
    implementation(libs.vico)
    debugImplementation(libs.bundles.compose.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
}
