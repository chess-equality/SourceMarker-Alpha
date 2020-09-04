plugins {
    kotlin("multiplatform")
    id("com.apollographql.apollo").version("2.3.0")
}

kotlin {
    jvm("jvm8") {
        compilations["main"].kotlinOptions.jvmTarget = "1.8"
    }
    js {
        browser { }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("com.apollographql.apollo:apollo-api:2.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

apollo {
    generateKotlinModels.set(true)
    rootPackageName.set("monitor.skywalking.protocol")
}

////todo: should be able to move to root project
//tasks {
//    withType<JavaCompile> {
//        sourceCompatibility = "1.8"
//        targetCompatibility = "1.8"
//    }
//    listOf("compileKotlin", "compileTestKotlin").forEach {
//        getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>(it) {
//            kotlinOptions.jvmTarget = "1.8"
//        }
//    }
//
//    withType<io.gitlab.arturbosch.detekt.Detekt> {
//        jvmTarget = "1.8"
//    }
//}
