package wcode.software.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import wcode.software.data.database.DatabaseConstants

object AuthorDB : UUIDTable(columnName = DatabaseConstants.Author.id) {
    val name = varchar(DatabaseConstants.Author.name, 100)
    val picUrl = text(DatabaseConstants.Author.picUrl).nullable()
}
