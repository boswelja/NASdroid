plugins {
    alias(libs.plugins.kotlin.multiplatform)

    alias(libs.plugins.detekt)
}

kotlin {
    jvm()
    jvmToolchain(17)

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}
