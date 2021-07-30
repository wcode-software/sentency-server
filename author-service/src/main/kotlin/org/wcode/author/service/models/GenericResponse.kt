package org.wcode.author.service.models

import io.micronaut.core.annotation.Introspected

@Introspected
data class GenericResponse(
    val success: Boolean = true,
    val error: String? = null
)
