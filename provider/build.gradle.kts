plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    maven(url = "https://www.jetbrains.com/intellij-repository/snapshots") { name = "intellij-snapshots" }
    maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies/") { name = "intellij-dependencies" }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.jetbrains:annotations:19.0.0")
    compileOnly("org.slf4j:slf4j-api:1.7.30")
    compileOnly("org.jetbrains.intellij.deps.jcef:jcef:77.1.18-g8e8d602-chromium-77.0.3865.120")
    compileOnly("com.jetbrains.intellij.platform:util-ui:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:analysis:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.java:java-analysis:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:ide:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:ide-impl:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:core:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:core-ui:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:editor:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:lang:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:project-model:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.groovy:groovy-psi:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:uast:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.java:java-indexing:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.java:java-indexing-impl:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:util:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.java:java-psi:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:extensions:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.jetbrains.intellij.platform:util-rt:202.5103-EAP-CANDIDATE-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61") {
        isTransitive = false
    }
    compileOnly("org.jetbrains.kotlin:kotlin-compiler:1.3.61") {
        isTransitive = false
    }
    testImplementation("junit:junit:4.12")
}

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

    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "1.8"
    }
}
