package org.wcode.plugins

import org.koin.dsl.module
import org.wcode.database.connections.SQLiteConnection
import org.wcode.database.dao.AuthorDAO
import org.wcode.database.dao.QuoteDAO

val daoModule = module {
    single { QuoteDAO(get()) }
    single { AuthorDAO(get()) }
}

val databaseModule = module {
    single { SQLiteConnection().init() }
}
