val kotlin_version: String by project
val exposed_version = "0.24.1"

plugins {
    kotlin("jvm") version "1.4.0"
}

group = "org.wcode"
version = "0.1.0"

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
    implementation("io.javalin", "javalin", "3.9.1")
    implementation("io.javalin", "javalin-openapi", "3.9.1")

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

    //SQL Connection
    implementation("org.xerial:sqlite-jdbc:3.30.1")

    //Tests jUnit5
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "wcode.software.ApplicationKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
