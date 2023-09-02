plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.androidx.room)

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

room {
    schemaDirectory("$projectDir/schemas")
}

ksp {
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.koin.android)
}
