package org.wcode.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.wcode.database.DatabaseConstants

object UserTable : UUIDTable() {
    val email = varchar(DatabaseConstants.User.email, 200).uniqueIndex()
    val password = varchar(DatabaseConstants.User.password, 200)
}
