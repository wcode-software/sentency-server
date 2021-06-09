val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val koinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
}

group = "org.wcode"
version = "1.2.1"
application {
    mainClass.set("org.wcode.ApplicationKt")
}

repositories {
    mavenCentral()
    maven {
        url  = uri("https://jitpack.io")
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
    implementation ("com.github.nielsfalk:ktor-swagger:0.7.0")

    //Test
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
