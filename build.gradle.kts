val exposed_version = "0.25.1"

plugins {
    kotlin("jvm") version "1.4.0"
    jacoco
    id("org.sonarqube") version "3.0"
}

group = "org.wcode"
version = "0.2.0"

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/kotlin/exposed")
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    //Javalin
    implementation("io.javalin", "javalin", "3.11.0")
    implementation("io.javalin", "javalin-openapi", "3.11.0")

    // OpenAPI
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
    implementation("io.swagger.core.v3", "swagger-core", "2.0.9")
    implementation("org.webjars", "swagger-ui", "3.24.3")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.10.1")

    // Database
    implementation("org.jetbrains.exposed", "exposed-core", exposed_version)
    implementation("org.jetbrains.exposed", "exposed-dao", exposed_version)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposed_version)
    implementation("org.jetbrains.exposed", "exposed-jodatime", exposed_version)

    // JWT
    implementation("com.auth0:java-jwt:3.11.0")

    //SQL Connection
    implementation("org.xerial:sqlite-jdbc:3.30.1")

    // PostgreSQL
    implementation("org.postgresql:postgresql:42.2.2")

    // Dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.1")

    // Bcrypt
    implementation("at.favre.lib:bcrypt:0.9.0")

    //Tests jUnit5
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}


tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "wcode.software.ApplicationKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = true
    }
    finalizedBy(tasks.sonarqube)
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

sonarqube {
    properties {
        property("sonar.projectKey", "walterjgsp_sentency-server")
        property("sonar.core.codeCoveragePlugin", "jacoco")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "walterjgsp-github")
        property("sonar.login", "1f9a62bc1175a7ec91a428c3899fa033167ddd05")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.tests", "test")
        property("sonar.sources", "src")
    }
}