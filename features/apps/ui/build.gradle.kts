plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.apps.ui"

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
    implementation(projects.core.composeLogviewer)
    implementation(projects.core.design)
    implementation(projects.core.navigation)
    implementation(projects.core.skeleton)

    implementation(projects.features.apps.logic)

    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    implementation(libs.compose.material3.adaptive.navigation)
    implementation(libs.compose.markdown)
    implementation(libs.compose.menuprovider)
    debugImplementation(libs.bundles.compose.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}
