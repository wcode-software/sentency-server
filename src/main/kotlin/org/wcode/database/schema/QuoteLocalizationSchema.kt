package org.wcode.database.schema

import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.wcode.database.core.BaseSchema
import org.wcode.database.tables.QuoteLocalizationTable
import org.wcode.dto.QuoteLocalizationDTO
import java.util.*

class QuoteLocalizationSchema(id: EntityID<UUID>) : BaseSchema<QuoteLocalizationDTO>(id) {

    companion object : UUIDEntityClass<QuoteLocalizationSchema>(QuoteLocalizationTable)

    var message by QuoteLocalizationTable.message
    var code by QuoteLocalizationTable.code

    var quote by QuoteSchema referencedOn QuoteLocalizationTable.quote

    override fun toDTO(): QuoteLocalizationDTO = QuoteLocalizationDTO(
        id = this.id.toString(),
        message = this.message,
        code = this.code,
        quoteId = quote.id.toString()
    )
}

