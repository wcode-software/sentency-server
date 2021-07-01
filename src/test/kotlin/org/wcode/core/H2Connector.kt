package org.wcode.core

import org.jetbrains.exposed.sql.Database
import org.wcode.database.sql.core.BaseSQLConnector

class H2Connector : BaseSQLConnector() {

    override fun connectToDB(): Database {
        return Database.connect(url = "jdbc:h2:mem:sentencydb;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }
}
