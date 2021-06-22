package org.wcode.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import org.wcode.database.connections.PostgreSQLConnection
import org.wcode.database.connections.SQLiteConnection
import org.wcode.database.core.BaseConnection
import org.wcode.database.dao.AuthorDAO
import org.wcode.database.dao.QuoteDAO
import org.wcode.core.EnvironmentConfig
import org.wcode.database.dao.QuoteLocalizationDAO
import org.wcode.database.dao.UserDAO

fun Application.configureDependencyInjection() {
    install(Koin) {
        SLF4JLogger()
        modules(databaseModule, daoModule)
    }
    install(ContentNegotiation) {
        json()
    }
}

val daoModule = module {
    single { QuoteDAO(get()) }
    single { AuthorDAO(get()) }
    single { UserDAO(get()) }
    single { QuoteLocalizationDAO(get()) }
}

val databaseModule = module {
    single { createDB().init() }
}

fun createDB(): BaseConnection {
    return if (EnvironmentConfig.flavor == "development")
        SQLiteConnection()
    else
        PostgreSQLConnection(
            name = EnvironmentConfig.databaseName,
            user = EnvironmentConfig.databaseUsername,
            password = EnvironmentConfig.databasePassword
        )
}
