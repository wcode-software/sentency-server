package wcode.software.database.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wcode.software.database.schema.QuoteDB
import wcode.software.database.tables.AuthorDB
import wcode.software.models.QuoteSchema
import java.util.*

class AuthorSchema(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<AuthorSchema>(AuthorDB)

    var name by AuthorDB.name
    var picUrl by AuthorDB.picUrl

    val quotes by QuoteSchema referrersOn QuoteDB.author
}