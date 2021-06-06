package org.wcode.database.core

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.tables.AuthorTable
import org.wcode.database.tables.QuoteTable
import org.wcode.database.tables.UserTable

abstract class BaseConnection {
    private val tables = listOf(AuthorTable, QuoteTable, UserTable)

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
