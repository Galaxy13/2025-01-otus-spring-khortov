import name.remal.gradle_plugins.sonarlint.SonarLintExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("java")
    id("name.remal.sonarlint") version "5.1.3"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
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
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("com.opencsv:opencsv:5.10")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

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
}

tasks.test {
    useJUnitPlatform()
}