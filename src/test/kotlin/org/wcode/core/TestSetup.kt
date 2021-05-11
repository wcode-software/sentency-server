package org.wcode.core

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import org.wcode.database.connections.H2Connection
import org.wcode.plugins.configureHTTP
import org.wcode.plugins.configureRouting
import org.wcode.plugins.daoModule

fun Application.configureTestModules() {
    install(Koin) {
        SLF4JLogger()
        modules(databaseTestModule, daoModule)
    }
    install(ContentNegotiation) {
        json()
    }
}

val databaseTestModule = module {
    single { H2Connection().init() }
}

fun Application.setupTestApplication() {
    configureTestModules()
    configureRouting()
    configureHTTP()
}
