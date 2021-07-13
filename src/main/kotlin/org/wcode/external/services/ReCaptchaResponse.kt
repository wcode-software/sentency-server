package org.wcode.external.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReCaptchaResponse(
    val success: Boolean,
    val score: Double,
    val action: String,
    @SerialName("challenge_ts")
    val challengeTs: String,
    val hostname: String
)
