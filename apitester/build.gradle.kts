import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.multiplatform)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.apitester"

    defaultConfig {
        val workflowRun: Int? by project
        applicationId = "com.nasdroid.apitester"
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

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/versions/9/previous-compilation-data.bin",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
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

    androidTarget()
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.api)
                implementation(compose.material3)
                implementation(libs.compose.menuprovider)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.activity.compose)
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

/**
 * Calculates an appropriate version name, based on [CalVer](https://calver.org/).
 */
fun calculateVersionName(): String {
    val formatter = DateTimeFormatter.ofPattern("YYYY.w")
    val nowDate = LocalDate.now()
    return nowDate.format(formatter)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
