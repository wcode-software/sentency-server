package org.wcode.database.nosql.dao

import com.mongodb.client.MongoDatabase
import io.ktor.features.*
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.util.idValue
import org.wcode.database.nosql.CollectionsConstants
import org.wcode.database.nosql.models.QuoteLocalizationMongo
import org.wcode.dto.QueueDTO
import org.wcode.dto.QuoteLocalizationDTO

class QueueLocalizationDAO(private val database: MongoDatabase) {

    fun insert(quoteLocalizationDTO: QuoteLocalizationDTO) {
        val collection = database.getCollection<QuoteLocalizationMongo>(CollectionsConstants.language)
        val newObject = QuoteLocalizationMongo(quoteLocalizationDTO)
        collection.insertOne(newObject)
    }

    fun drop(id: String): Boolean {
        val collection = database.getCollection(CollectionsConstants.language)
        val query = collection.deleteOneById(ObjectId(id))
        return query.deletedCount == 1L
    }

    fun listAll(): List<QueueDTO<QuoteLocalizationDTO>> {
        val collection = database.getCollection<QuoteLocalizationMongo>(CollectionsConstants.language)
        return collection.find().toList().map {
            it.toDTO()
        }
    }

    fun getById(id: String): Result<QueueDTO<QuoteLocalizationDTO>> {
        val collection = database.getCollection<QuoteLocalizationMongo>(CollectionsConstants.language)
        val query = collection.findOneById(ObjectId(id))
        return if (query != null) {
            Result.success(query.toDTO());
        } else {
            Result.failure(NotFoundException("Item not found on queue"))
        }
    }

}
