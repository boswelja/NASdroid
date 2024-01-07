plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sqldelight)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.apps.data"

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
        create("AppsDatabase")
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    implementation(projects.core.api)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.koin.core)

    implementation(libs.sqldelight.driver.android)
    implementation(libs.sqldelight.extensions.coroutines)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.sqldelight.driver.jvm)
}
