plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.detekt)
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
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}
