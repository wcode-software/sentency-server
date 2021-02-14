package wcode.software.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import wcode.software.data.database.DatabaseConstants

object QuoteDB : UUIDTable() {
    val author = reference(DatabaseConstants.Quote.author, AuthorDB, onDelete = ReferenceOption.CASCADE)
    val quote = text(DatabaseConstants.Quote.quote)
    val timestap = long(DatabaseConstants.Quote.timestamp)
    val type = varchar(DatabaseConstants.Quote.type, 100).nullable()
}
