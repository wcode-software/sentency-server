package wcode.software.dtos

import wcode.software.base.BaseDTO
import wcode.software.models.Quote
import java.security.Timestamp
import java.util.*

data class QuoteDTO(
    val id: String? = null,
    val authorId: String,
    val quote: String,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) : BaseDTO {
    constructor(_quote: Quote) : this(
        id = _quote.id.toString(),
        authorId = _quote.author.id.toString(),
        quote = _quote.quote,
        timestamp = _quote.timestamp
    )
}