plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.power.ui"

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
    jvmToolchain(17)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    implementation(projects.core.design)
    implementation(projects.features.power.logic)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
}
