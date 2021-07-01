package org.wcode.database.nosql.models

import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.wcode.dto.QueueDTO
import org.wcode.dto.QuoteLocalizationDTO
import java.util.*

data class QuoteLocalizationMongo(
    val id: String,
    val code: String,
    val message: String,
    val quoteId: String,
    val timestamp: Long = Calendar.getInstance().timeInMillis,
    val _id: Id<QuoteLocalizationMongo> = newId(),
) {

    constructor(quoteLocalizationDTO: QuoteLocalizationDTO) : this(
        id = quoteLocalizationDTO.id,
        code = quoteLocalizationDTO.code,
        message = quoteLocalizationDTO.message,
        quoteId = quoteLocalizationDTO.quoteId
    )

    fun toDTO(): QueueDTO<QuoteLocalizationDTO> = QueueDTO(
        id = _id.toString(),
        data = QuoteLocalizationDTO(
            id, code, message, quoteId
        ),
        timestamp = timestamp,
    )
}
