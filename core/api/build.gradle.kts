plugins {
    alias(libs.plugins.kotlin.multiplatform)

    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.boswelja.truemanager.core.api"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        sarifReport = true
        htmlReport = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)

    android()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.datetime)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.serialization.json)

                implementation(libs.kotlinx.serialization.json)

                implementation(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}
