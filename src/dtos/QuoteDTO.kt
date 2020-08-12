package wcode.software.dtos

import wcode.software.base.BaseDTO
import wcode.software.models.Quote

data class QuoteDTO(
    val id: String? = null,
    val authorId: String,
    val quote: String
) : BaseDTO {
    constructor(quote: Quote) : this(
        id = quote.id.toString(),
        quote = quote.quote,
        authorId = quote.author.id.toString()
    )
}