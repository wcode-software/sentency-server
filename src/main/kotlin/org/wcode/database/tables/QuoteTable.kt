package org.wcode.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.wcode.database.DatabaseConstants

object QuoteTable : UUIDTable() {
    val author = reference(DatabaseConstants.Quote.author, AuthorTable, onDelete = ReferenceOption.CASCADE)
    val timestap = long(DatabaseConstants.Quote.timestamp)
    val type = varchar(DatabaseConstants.Quote.type, 100).nullable()
}
