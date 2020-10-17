package wcode.software.dtos

import wcode.software.base.BaseDTO
import wcode.software.models.QuoteSchema
import java.util.*

data class QuoteDTO(
    val id: String? = null,
    val authorId: String,
    val quote: String,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) : BaseDTO {
    constructor(_quoteSchema: QuoteSchema) : this(
        id = _quoteSchema.id.toString(),
        authorId = _quoteSchema.author.id.toString(),
        quote = _quoteSchema.quote,
        timestamp = _quoteSchema.timestamp
    )
}