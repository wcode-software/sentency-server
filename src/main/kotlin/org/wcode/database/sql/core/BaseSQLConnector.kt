package org.wcode.database.sql.core

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.sql.tables.AuthorTable
import org.wcode.database.sql.tables.QuoteLocalizationTable
import org.wcode.database.sql.tables.QuoteTable
import org.wcode.database.sql.tables.UserTable

abstract class BaseSQLConnector {
    private val tables = listOf(AuthorTable, QuoteTable, UserTable, QuoteLocalizationTable)

    fun init(): Database {
        val db = connectToDB()
        createTables(db)
        return db
    }

    abstract fun connectToDB(): Database

    private fun createTables(database: Database) {
        transaction(database) {
            tables.forEach { table ->
                SchemaUtils.create(table)
            }
        }
    }
}
