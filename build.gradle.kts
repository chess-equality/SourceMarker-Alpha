plugins {
    val kotlinVersion = "1.4.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("js") version kotlinVersion apply false

    id("io.gitlab.arturbosch.detekt") version "1.11.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.11.0")
}

detekt {
    config = files("./detekt-config.yml")
    buildUponDefaultConfig = true

    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
    }
}

//
//subprojects {
//    group = "com.sourceplusplus"
//    version = "1.0-SNAPSHOT"
//
//    repositories {
//        mavenCentral()
//        maven(url = "https://jitpack.io") { name = "jitpack" }
//        maven(url = "https://jcenter.bintray.com") { name = "jcenter" }
//    }
//}