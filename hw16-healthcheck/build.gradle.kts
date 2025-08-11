import name.remal.gradle_plugins.sonarlint.SonarLintExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("java")
    id("name.remal.sonarlint") version "5.1.10"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.node-gradle.node") version "7.0.2"
    checkstyle
}

checkstyle {
    config = resources.text.fromUri("https://raw.githubusercontent.com/OtusTeam/Spring/master/checkstyle.xml")
}

group = "com.galaxy13"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("com.h2database:h2")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.reflections:reflections:0.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom(BOM_COORDINATES)
    }
}

configure<SonarLintExtension> {
    nodeJs {
        detectNodeJs = false
        logNodeJsNotFound = false
    }
    languages {
        include("JavaScript")
    }
    sonarProperty("sonar.sources", "src/main/java,src/test/java,src/ui")
    sonarProperty("sonar.exclusions", "**/generated/**,**/build/**,**/node_modules/**")
    sonarProperty("sonar.javascript.file.suffixes", ".js, .vue")
}

tasks.test {
    useJUnitPlatform()
}

val viteBuild = tasks.register<com.github.gradle.node.npm.task.NpmTask>("viteBuild") {
    dependsOn(tasks.npmInstall)
    workingDir.set(file("src/ui"))
    environment = mapOf(
        "NODE_ENV" to "production",
        "FORCE_COLOR" to "true",
        "LANG" to "en_US.UTF-8"
    )
    npmCommand.set(listOf("run", "build"))
    inputs.dir("$projectDir/src/ui/")
    outputs.dir("$projectDir/src/main/resources/static")
}

tasks.named<com.github.gradle.node.npm.task.NpmInstallTask>("npmInstall") {
    workingDir.set(file("src/ui"))
}

node {
    download = true
    version = "22.15.0"
}

tasks.processResources {
    dependsOn(viteBuild)
}
