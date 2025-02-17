import name.remal.gradle_plugins.sonarlint.SonarLintExtension

plugins {
    id("java")
    id("name.remal.sonarlint") version "5.1.3"
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