package wcode.software.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import wcode.software.database.schema.QuoteDB
import wcode.software.database.tables.AuthorDB
import java.io.File
import java.sql.Connection

object DatabaseFactory {

    fun init() {
        connectSQLite()
        transaction {
            create(AuthorDB)
            create(QuoteDB)
        }
    }

    private fun connectSQLite() {
        val filename = File("sentency.db").absolutePath
        Database.connect("jdbc:sqlite:$filename", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }
}