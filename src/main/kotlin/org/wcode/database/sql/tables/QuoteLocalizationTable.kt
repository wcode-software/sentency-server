package org.wcode.database.sql.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.wcode.database.sql.DatabaseConstants
import org.wcode.database.sql.tables.AuthorTable.nullable

object QuoteLocalizationTable : UUIDTable() {
    val quote = reference(DatabaseConstants.QuoteLocalization.quoteId, QuoteTable, onDelete = ReferenceOption.CASCADE)
    val code = QuoteLocalizationTable.varchar(DatabaseConstants.QuoteLocalization.code, 250)
    val message = QuoteLocalizationTable.text(DatabaseConstants.QuoteLocalization.message)
}
