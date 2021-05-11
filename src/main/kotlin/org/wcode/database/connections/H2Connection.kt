package org.wcode.database.connections

import org.jetbrains.exposed.sql.Database
import org.wcode.database.core.BaseConnection

class H2Connection : BaseConnection() {

    override fun connectToDB(): Database {
        return Database.connect(url = "jdbc:h2:mem:sentencydb;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }
}
