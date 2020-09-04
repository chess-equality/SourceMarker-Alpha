plugins {
    id("org.jetbrains.kotlin.jvm")
}

//todo: remove dependency on intellij
repositories {
    maven(url = "https://jitpack.io") { name = "jitpack" }
    maven(url = "https://www.jetbrains.com/intellij-repository/releases") { name = "intellij-releases" }
}

dependencies {
    val intellijVersion = "202.6948.69"
    val kotlinVersion = "1.4.0"

    implementation(project(":protocol"))
    compileOnly(project(":marker")) //todo: only need MarkerUtils
    implementation("com.github.sh5i:git-stein:v0.5.0")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.8.0.202006091008-r")

    compileOnly("com.jetbrains.intellij.platform:util-ui:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:analysis:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-analysis:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-uast:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:ide:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:ide-impl:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:core:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:core-impl:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:core-ui:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:editor:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:lang:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:project-model:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.groovy:groovy-psi:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:uast:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-indexing:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-indexing-impl:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:util:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-psi:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:extensions:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:util-rt:$intellijVersion") { isTransitive = false }
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion") { isTransitive = false }
    compileOnly("org.jetbrains.kotlin:kotlin-compiler:$kotlinVersion") { isTransitive = false }

    testImplementation("junit:junit:4.13")
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
