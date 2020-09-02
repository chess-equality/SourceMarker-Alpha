plugins {
    id("com.avast.gradle.docker-compose") version "0.12.1"

    val kotlinVersion = "1.4.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("js") version kotlinVersion apply false

//    id("io.gitlab.arturbosch.detekt") version "1.11.0"
//    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginName: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion

subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

dependencies {
//    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.11.0")
}

//detekt {
//    failFast = true
//    config = files("config/detekt/detekt.yml")
//    buildUponDefaultConfig = true
//
//    reports {
//        html.enabled = false
//        xml.enabled = false
//        txt.enabled = true
//    }
//}

gradle.buildFinished {
    project.buildDir.deleteRecursively()
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

tasks {
    register("downloadSkywalking") {
        doLast {
            println("Downloading Apache SkyWalking")
            val f = File(projectDir, "test/apache-skywalking-apm-es7-8.1.0.tar.gz")
            if (!f.exists()) {
                java.net.URL("https://downloads.apache.org/skywalking/8.1.0/apache-skywalking-apm-es7-8.1.0.tar.gz")
                    .openStream().use { input ->
                        java.io.FileOutputStream(f).use { output ->
                            input.copyTo(output)
                        }
                    }
            }
            println("Downloaded Apache SkyWalking")
        }
    }
}

dockerCompose {
    dockerComposeWorkingDirectory = "./test"
    useComposeFiles = listOf("./docker-compose.yml")
    captureContainersOutput = true
}
