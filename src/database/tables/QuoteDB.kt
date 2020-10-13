package wcode.software.database.schema

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.date
import wcode.software.database.DatabaseConstants
import wcode.software.database.tables.AuthorDB

object QuoteDB : UUIDTable() {
    val author = reference(DatabaseConstants.Quote.author, AuthorDB, onDelete = ReferenceOption.CASCADE)
    val quote = text(DatabaseConstants.Quote.quote)
    val timestap = long(DatabaseConstants.Quote.timestamp)
}