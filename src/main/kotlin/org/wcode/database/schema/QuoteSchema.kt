package org.wcode.database.schema

import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.wcode.database.core.BaseSchema
import org.wcode.database.tables.QuoteLocalizationTable
import org.wcode.database.tables.QuoteTable
import org.wcode.dto.QuoteDTO
import java.util.*

class QuoteSchema(id: EntityID<UUID>) : BaseSchema<QuoteDTO>(id) {

    companion object : UUIDEntityClass<QuoteSchema>(QuoteTable)

    var timestamp by QuoteTable.timestap
    var type by QuoteTable.type

    var author by AuthorSchema referencedOn QuoteTable.author
    val messages by QuoteLocalizationSchema referrersOn QuoteLocalizationTable.quote

    override fun toDTO(): QuoteDTO = QuoteDTO(
        id = this.id.toString(),
        messages = this.messages.map { it.toDTO() },
        authorId = this.author.id.toString(),
        timestamp = this.timestamp,
        author = this.author.toDTO(),
        type = this.type
    )
}
