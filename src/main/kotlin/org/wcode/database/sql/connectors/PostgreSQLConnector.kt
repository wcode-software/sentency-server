package org.wcode.database.sql.connectors

import org.jetbrains.exposed.sql.Database
import org.wcode.database.sql.core.BaseSQLConnector

class PostgreSQLConnector(
    val name: String,
    val user: String,
    val password: String
) : BaseSQLConnector() {
    override fun connectToDB(): Database {
        return Database.connect(
            "jdbc:postgresql://sentency_database:5432/${name}",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }
}
