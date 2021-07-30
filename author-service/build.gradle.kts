plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    id("org.jetbrains.kotlin.kapt") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "1.5.3"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.32"
}

version = "1.0.0"
group = "org.wcode.author.service"

val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("org.wcode.author.service.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")

    compileOnly("org.graalvm.nativeimage:svm")

    implementation("io.micronaut:micronaut-validation")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Database
    kapt("io.micronaut.data:micronaut-data-processor:2.4.7")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    testRuntimeOnly("com.h2database:h2")
    runtime("io.micronaut.sql:micronaut-jdbc-tomcat")


}

application {
    mainClass.set("org.wcode.author.service.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }
    nativeImage {
        args("--no-fallback")
        setImageName("author-service")
    }
}
