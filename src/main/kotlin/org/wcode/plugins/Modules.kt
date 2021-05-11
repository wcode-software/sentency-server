package org.wcode.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureModules() {
    install(Koin) {
        SLF4JLogger()
        modules(databaseModule, daoModule)
    }
    install(ContentNegotiation) {
        json()
    }
}
