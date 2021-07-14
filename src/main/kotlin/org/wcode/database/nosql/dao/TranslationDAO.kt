package org.wcode.database.nosql.dao

import com.mongodb.client.MongoDatabase
import io.ktor.features.*
import org.litote.kmongo.*
import org.wcode.database.nosql.CollectionsConstants
import org.wcode.database.nosql.models.TranslationMongo

class TranslationDAO(private val database: MongoDatabase) {

    fun insert(translationMongo: TranslationMongo) {
        val collection = database.getCollection<TranslationMongo>(CollectionsConstants.translation)
        collection.insertOne(translationMongo)
    }

    fun drop(code: String, quoteId: String): Boolean {
        val collection = database.getCollection(CollectionsConstants.translation)
        val query = collection.deleteOne("{code: \"$code\", quoteId: \"$quoteId\"}")
        return query.deletedCount == 1L
    }

    fun getByCodeId(code: String, quoteId: String): Result<TranslationMongo> {
        val collection = database.getCollection<TranslationMongo>(CollectionsConstants.translation)
        val query = collection.findOne(TranslationMongo::code eq code, TranslationMongo::quoteId eq quoteId)
        return if (query != null) {
            Result.success(query);
        } else {
            Result.failure(NotFoundException("Item not found on queue"))
        }
    }
}
