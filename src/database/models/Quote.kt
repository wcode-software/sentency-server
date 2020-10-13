package wcode.software.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wcode.software.database.models.Author
import wcode.software.database.schema.QuoteDB
import java.util.*

class Quote(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Quote>(QuoteDB)

    var author by Author referencedOn QuoteDB.author
    var quote by QuoteDB.quote
    var timestamp by QuoteDB.timestap
}
