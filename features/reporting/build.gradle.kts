plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.reporting"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
    implementation(projects.core.api)
    implementation(projects.core.design)

    implementation(libs.androidx.window)

    implementation(libs.vico)

    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
}
