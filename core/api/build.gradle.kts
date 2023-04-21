plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
}

kotlin {
    jvmToolchain(17)
}

detekt {
    buildUponDefaultConfig = true
    config = files("$rootDir/config/detekt.yml")
    baseline = file("$projectDir/detekt-baseline.xml")
}

dependencies {
    // Ktor
    implementation(libs.bundles.ktor.client.android)
    implementation(libs.ktor.logging)

    api(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)
}
