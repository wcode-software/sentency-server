package org.wcode.database.connections

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.wcode.database.core.BaseConnection
import java.io.File
import java.sql.Connection

class SQLiteConnection : BaseConnection() {

    override fun connectToDB(): Database {
        val filename = File("sentency.db").absolutePath
        val database = Database.connect("jdbc:sqlite:$filename", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return database
    }
}
