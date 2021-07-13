package org.wcode.external.models

import kotlinx.serialization.Serializable

@Serializable
data class ReCaptchaDTO(
    val token: String,
    val action: String
)
