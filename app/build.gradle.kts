import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid"

    defaultConfig {
        val workflowRun: Int? by project
        applicationId = "com.nasdroid"
        versionCode = workflowRun ?: 1
        versionName = calculateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        register("release") {
            val storeFilePath: String? by project
            val storePass: String? by project
            val key: String? by project
            val keyPass: String? by project
            this.storeFile = storeFilePath?.let { file(it) }
            this.storePassword = storePass
            this.keyAlias = key
            this.keyPassword = keyPass
        }
    }

    buildTypes {
        release {
            // Only apply release config in CI, otherwise we need to configure it when running release builds.
            signingConfig = if (System.getenv("CI") == "true") {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
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
    implementation(projects.core.design)

    implementation(projects.features.apps.ui)
    implementation(projects.features.auth.ui)
    implementation(projects.features.dashboard.ui)
    implementation(projects.features.power.ui)
    implementation(projects.features.reporting.ui)
    implementation(projects.features.storage.ui)

    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.koin.android)

    // Compose
    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    implementation(libs.compose.menuprovider)
    debugImplementation(libs.bundles.compose.tooling)
    androidTestImplementation(libs.compose.ui.test.junit4)

    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
}

/**
 * Calculates an appropriate version name, based on [CalVer](https://calver.org/).
 */
fun calculateVersionName(): String {
    val formatter = DateTimeFormatter.ofPattern("YYYY.w")
    val nowDate = LocalDate.now()
    return nowDate.format(formatter)
}
