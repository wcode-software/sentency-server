package wcode.software.data.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.TransactionManager
import wcode.software.config.Environment
import wcode.software.data.database.tables.UserDB
import wcode.software.data.database.tables.QuoteDB
import wcode.software.data.database.tables.AuthorDB
import java.io.File
import java.sql.Connection

object DatabaseFactory {

    fun init() {
        connectToDB()
        transaction {
            create(AuthorDB)
            create(QuoteDB)
            create(UserDB)
        }
    }

    private fun connectToDB(){
        if(Environment.environment == "development"){
            connectSQLite()
        }else{
            connectPostgre()
        }
    }

    private fun connectSQLite() {
        val filename = File("sentency.db").absolutePath
        Database.connect("jdbc:sqlite:$filename", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    private fun connectPostgre(){
        Database.connect("jdbc:postgresql://sentency_database:5432/${Environment.dbName}", driver = "org.postgresql.Driver",
            user = Environment.dbUser, password = Environment.dbPassword)
    }
}
