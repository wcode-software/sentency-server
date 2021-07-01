package org.wcode.database.sql.connectors

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.wcode.database.sql.core.BaseSQLConnector
import java.io.File
import java.sql.Connection

class SQLiteConnector : BaseSQLConnector() {

    override fun connectToDB(): Database {
        val filename = File("sentency.db").absolutePath
        val database = Database.connect("jdbc:sqlite:$filename", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return database
    }
}
