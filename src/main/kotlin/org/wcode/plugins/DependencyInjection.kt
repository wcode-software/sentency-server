package org.wcode.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import org.wcode.database.sql.connectors.PostgreSQLConnector
import org.wcode.database.sql.connectors.SQLiteConnector
import org.wcode.database.sql.core.BaseSQLConnector
import org.wcode.core.EnvironmentConfig
import org.wcode.database.nosql.connectors.MongoConnection
import org.wcode.database.nosql.dao.QueueLocalizationDAO
import org.wcode.database.nosql.dao.TranslationDAO
import org.wcode.database.sql.dao.*
import org.wcode.external.services.GoogleService

fun Application.configureDependencyInjection() {
    install(Koin) {
        SLF4JLogger()
        modules(databaseModule, daoModule, serviceModule)
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
    single { QueueLocalizationDAO(get()) }
    single { TranslationDAO(get()) }
}

val databaseModule = module {
    single { createDB().init() }
    single { MongoConnection().init() }
}

val serviceModule = module {
    single { GoogleService(get(), get()) }
}

fun createDB(): BaseSQLConnector {
    return if (EnvironmentConfig.flavor == "development")
        SQLiteConnector()
    else
        PostgreSQLConnector(
            name = EnvironmentConfig.databaseName,
            user = EnvironmentConfig.databaseUsername,
            password = EnvironmentConfig.databasePassword
        )
}
