package org.wcode.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDTO(
    val token: String
)
