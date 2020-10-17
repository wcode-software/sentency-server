package wcode.software.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wcode.software.database.models.AuthorSchema
import wcode.software.database.schema.QuoteDB
import java.util.*

class QuoteSchema(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<QuoteSchema>(QuoteDB)

    var author by AuthorSchema referencedOn QuoteDB.author
    var quote by QuoteDB.quote
    var timestamp by QuoteDB.timestap
}
