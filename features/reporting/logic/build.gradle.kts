plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.reporting.logic"

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
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.datetime)
    api(libs.datatypes.bitrate)
    api(libs.datatypes.capacity)
    api(libs.datatypes.percentage)
    api(libs.datatypes.temperature)
    api(projects.core.strongResult)

    implementation(projects.core.api)
    implementation(projects.features.reporting.data)

    implementation(libs.koin.core)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
