package org.wcode.database.nosql.models

data class TranslationMongo(
    val quoteId: String,
    val message: String,
    val code: String
)
