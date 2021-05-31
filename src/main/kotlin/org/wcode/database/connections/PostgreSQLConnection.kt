package org.wcode.database.connections

import org.jetbrains.exposed.sql.Database
import org.wcode.database.core.BaseConnection

class PostgreSQLConnection(
    val name: String,
    val user: String,
    val password: String
) : BaseConnection() {
    override fun connectToDB(): Database {
        return Database.connect(
            "jdbc:postgresql://sentency_database:5432/${name}",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }
}
