import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val koinVersion: String by project
val jacocoVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
    jacoco
    id("org.sonarqube") version "3.2.0"
}

group = "org.wcode"
version = "1.2.1"
application {
    mainClass.set("org.wcode.ApplicationKt")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //Auth
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    //Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    //Database Connectors
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.postgresql:postgresql:42.2.20")

    //Koin
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    //Swagger
    implementation("com.github.nielsfalk:ktor-swagger:0.7.0")

    //Test
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

jacoco {
    toolVersion = jacocoVersion
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = true
        html.isEnabled = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
//    useJUnitPlatform {
//        includeEngines("junit-jupiter", "spek2")
//    }

    testLogging {
        exceptionFormat = FULL
        events("passed", "failed", "skipped")
    }
}

sonarqube {
    properties {

        val sonarToken = System.getenv()["SONAR_TOKEN"]

        property("sonar.sources", "src/main/kotlin")
        property("sonar.binaries", "build/intermediates/classes/debug")
        property("sonar.tests", "src/test/kotlin")
        property("sonar.jacoco.reportsPaths", "build/jacoco/test.exec")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.junit.reportsPath", "build/test-results/test")

        property("sonar.coverage.exclusions", "")

        property("sonar.projectKey", "walterjgsp_sentency-server")
        property("sonar.projectVersion", "$version")
        property("sonar.organization", "walterjgsp-github")
        property("sonar.projectName", "Sentency Server")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", "$sonarToken")
    }
}
