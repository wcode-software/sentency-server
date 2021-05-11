package org.wcode.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.wcode.database.DatabaseConstants

object AuthorTable: UUIDTable(columnName = DatabaseConstants.Author.id) {
    val name = varchar(DatabaseConstants.Author.name, 100)
    val picUrl = text(DatabaseConstants.Author.picUrl).nullable()
}
