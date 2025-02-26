import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import name.remal.gradle_plugins.sonarlint.SonarLintExtension

plugins {
    id("java")
    id("name.remal.sonarlint") version "5.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    implementation("org.springframework:spring-context:6.2.3")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("com.opencsv:opencsv:5.10")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.15.2")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

configure<SonarLintExtension> {
    nodeJs {
        detectNodeJs = false
        logNodeJsNotFound = false
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("testApp")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "com.galaxy13.hw.Application"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks {
    register<JavaExec>("run") {
        group = "application"

        dependsOn(shadowJar)
        classpath = files("build/libs/testApp.jar")
    }
}

tasks.test {
    useJUnitPlatform()
}