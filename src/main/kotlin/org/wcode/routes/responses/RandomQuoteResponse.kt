package org.wcode.routes.responses

import kotlinx.serialization.Serializable
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO

@Serializable
data class RandomQuoteResponse(
    val quote: QuoteDTO,
    val author: AuthorDTO
)
