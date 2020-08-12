package wcode.software.database.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wcode.software.database.schema.QuoteDB
import wcode.software.database.tables.AuthorDB
import wcode.software.models.Quote
import java.util.*

class Author(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Author>(AuthorDB)

    var name by AuthorDB.name
    var picUrl by AuthorDB.picUrl

    val quotes by Quote referrersOn QuoteDB.author
}