plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid"

    defaultConfig {
        applicationId = "com.nasdroid"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/versions/9/previous-compilation-data.bin"
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
    implementation(projects.core.api)

    implementation(projects.features.apps.ui)
    implementation(projects.features.auth.ui)
    implementation(projects.features.dashboard.ui)
    implementation(projects.features.reporting)
    implementation(projects.features.storage.ui)

    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.koin.android)

    // Compose
    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    implementation(libs.compose.menuprovider)
    debugImplementation(libs.bundles.compose.tooling)
    androidTestImplementation(libs.compose.ui.test.junit4)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}