plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.sqldelight)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.auth.data"

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

sqldelight {
    databases {
        create("AuthDatabase") {
            packageName.set("com.nasdroid.auth.data.serverstore.sqldelight")
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    // SQLDelight
    implementation(libs.sqldelight.driver.android)
    implementation(libs.sqldelight.extensions.coroutines)

    implementation(libs.koin.android)
}
