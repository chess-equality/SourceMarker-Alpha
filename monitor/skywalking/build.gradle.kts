plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.apollographql.apollo").version("2.3.0")
}

dependencies {
    implementation(project(":protocol"))
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("com.apollographql.apollo:apollo-runtime:2.3.0")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.3.0")
    api("com.apollographql.apollo:apollo-api:2.3.0")
}

//todo: should be able to move to root project
tasks {
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    listOf("compileKotlin", "compileTestKotlin").forEach {
        getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>(it) {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

//    withType<io.gitlab.arturbosch.detekt.Detekt> {
//        jvmTarget = "1.8"
//    }
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
