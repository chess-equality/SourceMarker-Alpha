plugins {
    id("org.jetbrains.kotlin.jvm")
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
